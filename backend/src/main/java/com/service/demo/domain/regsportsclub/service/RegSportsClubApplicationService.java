package com.service.demo.domain.regsportsclub.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.demo.common.constant.SessionConst;
import com.service.demo.common.error.ErrorCode;
import com.service.demo.common.error.TokenErrorCode;
import com.service.demo.common.exception.ApiException;
import com.service.demo.domain.commoncode.mapper.CommonCodeMapper;
import com.service.demo.domain.commoncode.service.CommonCodeLookupService;
import com.service.demo.domain.regsportsclub.constant.RegSportsClubApplyStatus;
import com.service.demo.domain.regsportsclub.dto.RegSportsClubApplicationResponse;
import com.service.demo.domain.regsportsclub.dto.RegSportsClubApplicationUpsertRequest;
import com.service.demo.domain.regsportsclub.entity.ApplicationActionLogEntity;
import com.service.demo.domain.regsportsclub.entity.RegSportsClubApplyEntity;
import com.service.demo.domain.regsportsclub.entity.RegSportsClubApplicationCategoryEntity;
import com.service.demo.domain.regsportsclub.entity.RegSportsClubApplicationEntity;
import com.service.demo.domain.regsportsclub.mapper.ApplicationActionLogMapper;
import com.service.demo.domain.regsportsclub.mapper.RegSportsClubApplicationMapper;
import com.service.demo.domain.role.entity.UserRoleEntity;
import com.service.demo.domain.role.mapper.UserRoleMapper;
import com.service.demo.domain.sportsclub.entity.SportsClubCategoryEntity;
import com.service.demo.domain.sportsclub.entity.SportsClubEntity;
import com.service.demo.domain.sportsclub.mapper.SportsClubMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Service
@RequiredArgsConstructor
public class RegSportsClubApplicationService {
    private final RegSportsClubApplicationMapper regSportsClubApplicationMapper;
    private final SportsClubMapper sportsClubMapper;
    private final CommonCodeMapper commonCodeMapper;
    private final CommonCodeLookupService commonCodeLookupService;
    private final ActionService actionService;
    private final ApplicationActionLogMapper applicationActionLogMapper;
    private final UserRoleMapper userRoleMapper;
    private final ObjectMapper objectMapper;

    @Value("${camunda.orchestration.process-definition-id}")
    private String processDefinitionId;

    @Transactional
    public RegSportsClubApplicationResponse save(RegSportsClubApplicationUpsertRequest req) {
        Long applyId = upsert(req, RegSportsClubApplyStatus.SAVED);
        actionService.handleAction(applyId, com.service.demo.domain.regsportsclub.constant.Action.SAVE);
        return getByApplyId(applyId);
    }

    @Transactional
    public RegSportsClubApplicationResponse apply(RegSportsClubApplicationUpsertRequest req) {
        // 등록 스포츠클럽 신청 정보를 저장하고 상태를 "APPLY"로 변경
        Long applyId = upsert(req, RegSportsClubApplyStatus.APPLY);
        
        // bmp 상태 전이 수행
        actionService.handleAction(applyId, com.service.demo.domain.regsportsclub.constant.Action.APPLY);
        return getByApplyId(applyId);
    }

    @Transactional
    public void handleAction(Long applyId, String actionKey, Map<String, Object> payload) {
        Long userId = resolveCurrentUserId();
        List<UserRoleEntity> roles = userRoleMapper.findAllWithRole(userId, false);
        List<String> roleCodes = new ArrayList<>();
        for (UserRoleEntity role : roles) {
            if (role.getRoleCode() != null) {
                roleCodes.add(role.getRoleCode());
            }
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put("action", actionKey);
        variables.put("roles", roleCodes);
        if (payload != null && !payload.isEmpty()) {
            variables.put("payload", payload);
        }

        String taskKey = actionService.handleAction(applyId, actionKey, variables);

        ApplicationActionLogEntity log = new ApplicationActionLogEntity();
        log.setApplyId(applyId);
        log.setProcessCode(processDefinitionId);
        log.setActionKey(actionKey);
        log.setTaskKey(taskKey);
        log.setPayloadJson(serializePayload(payload));
        log.setActorId(userId);
        log.setActorRole(roleCodes.isEmpty() ? "UNKNOWN" : String.join(",", roleCodes));
        log.setExecutedAt(LocalDateTime.now());
        applicationActionLogMapper.insert(log);
    }

    public RegSportsClubApplicationResponse getByApplyId(Long applyId) {
        RegSportsClubApplicationResponse response = regSportsClubApplicationMapper.findApplicationDetail(applyId);
        if (response == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "Application not found");
        }
        List<Long> categoryIds = regSportsClubApplicationMapper.findCategoryIdsByApplicationId(response.getApplicationId());
        response.setOperatingSportCodeIds(categoryIds);
        return response;
    }

    public List<RegSportsClubApplicationResponse> list() {
        List<RegSportsClubApplicationResponse> responses = regSportsClubApplicationMapper.findAllApplications();
        if (responses.isEmpty()) {
            return responses;
        }
        List<Long> applicationIds = new ArrayList<>();
        for (RegSportsClubApplicationResponse response : responses) {
            if (response.getApplicationId() != null) {
                applicationIds.add(response.getApplicationId());
            }
        }
        if (!applicationIds.isEmpty()) {
            List<RegSportsClubApplicationCategoryEntity> pairs =
                    regSportsClubApplicationMapper.findCategoryPairsByApplicationIds(applicationIds);
            java.util.Map<Long, List<Long>> grouped = new java.util.HashMap<>();
            for (RegSportsClubApplicationCategoryEntity pair : pairs) {
                grouped.computeIfAbsent(pair.getApplicationId(), key -> new ArrayList<>())
                        .add(pair.getCategoryId());
            }
            for (RegSportsClubApplicationResponse response : responses) {
                List<Long> categoryIds = grouped.get(response.getApplicationId());
                response.setOperatingSportCodeIds(categoryIds);
            }
        }
        return responses;
    }

    private Long resolveCurrentUserId() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new ApiException(TokenErrorCode.AUTHORIZATION_TOKEN_NOT_FOUND);
        }
        Object userId = attributes.getAttribute(SessionConst.USER_ID, RequestAttributes.SCOPE_REQUEST);
        if (userId == null) {
            throw new ApiException(TokenErrorCode.AUTHORIZATION_TOKEN_NOT_FOUND);
        }
        if (userId instanceof Number) {
            return ((Number) userId).longValue();
        }
        return Long.parseLong(userId.toString());
    }

    private String serializePayload(Map<String, Object> payload) {
        try {
            if (payload == null || payload.isEmpty()) {
                return objectMapper.writeValueAsString(new HashMap<>());
            }
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "Payload serialization failed");
        }
    }

    private Long upsert(RegSportsClubApplicationUpsertRequest req, RegSportsClubApplyStatus defaultStatus) {
        Long statusCodeId = resolveStatusCodeId(req, defaultStatus);
        Long clubRoleCodeId = resolveClubRoleCodeId(req.getClubRoleCodeId());
        if (req.getApplyId() == null) {
            RegSportsClubApplyEntity applyEntity = new RegSportsClubApplyEntity();
            applyEntity.setStatusCodeId(statusCodeId);
            applyEntity.setAppliedAt(LocalDateTime.now());
            applyEntity.setApplicantName(req.getApplicantName());
            applyEntity.setApplicantTelno(req.getApplicantTelno());
            applyEntity.setApplicantEmail(req.getApplicantEmail());
            regSportsClubApplicationMapper.insertApply(applyEntity);

            RegSportsClubApplicationEntity applicationEntity = new RegSportsClubApplicationEntity();
            applicationEntity.setApplyId(applyEntity.getId());
            applicationEntity.setName(req.getClubName());
            applicationEntity.setLocation(req.getLocation());
            applicationEntity.setRepresentativeName(req.getRepresentativeName());
            applicationEntity.setRepresentativeTelno(req.getRepresentativeTelno());
            applicationEntity.setBusinessNo(req.getBusinessNo());
            applicationEntity.setClubRoleCodeId(clubRoleCodeId);
            applicationEntity.setOperatingSportParentCodeId(req.getOperatingSportParentCodeId());
            applicationEntity.setOperatingSportChildCodeId(req.getOperatingSportChildCodeId());
            regSportsClubApplicationMapper.insertApplication(applicationEntity);
            saveApplicationCategories(applicationEntity.getId(), req.getOperatingSportCodeIds(),
                    req.getOperatingSportParentCodeId(), req.getOperatingSportChildCodeId());

            return applyEntity.getId();
        }

        RegSportsClubApplyEntity applyEntity = regSportsClubApplicationMapper.findApplyById(req.getApplyId());
        if (applyEntity == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "Application not found");
        }
        applyEntity.setStatusCodeId(statusCodeId);
        applyEntity.setAppliedAt(LocalDateTime.now());
        applyEntity.setApplicantName(req.getApplicantName());
        applyEntity.setApplicantTelno(req.getApplicantTelno());
        applyEntity.setApplicantEmail(req.getApplicantEmail());
        regSportsClubApplicationMapper.updateApply(applyEntity);

        RegSportsClubApplicationEntity applicationEntity =
                regSportsClubApplicationMapper.findApplicationByApplyId(req.getApplyId());
        if (applicationEntity == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "Application detail not found");
        }
        applicationEntity.setName(req.getClubName());
        applicationEntity.setLocation(req.getLocation());
        applicationEntity.setRepresentativeName(req.getRepresentativeName());
        applicationEntity.setRepresentativeTelno(req.getRepresentativeTelno());
        applicationEntity.setBusinessNo(req.getBusinessNo());
        applicationEntity.setClubRoleCodeId(clubRoleCodeId);
        applicationEntity.setOperatingSportParentCodeId(req.getOperatingSportParentCodeId());
        applicationEntity.setOperatingSportChildCodeId(req.getOperatingSportChildCodeId());
        regSportsClubApplicationMapper.updateApplication(applicationEntity);
        saveApplicationCategories(applicationEntity.getId(), req.getOperatingSportCodeIds(),
                req.getOperatingSportParentCodeId(), req.getOperatingSportChildCodeId());

        return req.getApplyId();
    }

    private Long resolveStatusCodeId(RegSportsClubApplicationUpsertRequest req, RegSportsClubApplyStatus defaultStatus) {
        Long statusCodeId = req.getStatusCodeId();
        if (statusCodeId != null) {
            return statusCodeId;
        }
        String statusCode = req.getStatusCode();
        if (statusCode != null && !statusCode.isBlank()) {
            statusCodeId = commonCodeLookupService.getCodeId(
                    RegSportsClubApplyStatus.GROUP_CODE,
                    statusCode
            );
        }
        if (statusCodeId == null) {
            statusCodeId = commonCodeLookupService.getCodeId(
                    RegSportsClubApplyStatus.GROUP_CODE,
                    defaultStatus.getCode()
            );
        }
        if (statusCodeId == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "Apply status code not found");
        }
        return statusCodeId;
    }

    private Long resolveClubRoleCodeId(Long clubRoleCodeId) {
        if (clubRoleCodeId != null) {
            return clubRoleCodeId;
        }
        clubRoleCodeId = commonCodeMapper.findCodeIdByGroupCodeAndCode("CLUB_ROLE", "REG_CLUB");
        if (clubRoleCodeId == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "Club role code not found");
        }
        return clubRoleCodeId;
    }

    private void approveToSportsClub(Long applyId) {
        RegSportsClubApplicationEntity applicationEntity =
                regSportsClubApplicationMapper.findApplicationByApplyId(applyId);
        if (applicationEntity == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "Application detail not found");
        }
        if (applicationEntity.getApprovedClubId() != null) {
            return;
        }

        SportsClubEntity sportsClub = new SportsClubEntity();
        sportsClub.setName(applicationEntity.getName());
        sportsClub.setLocation(applicationEntity.getLocation());
        sportsClub.setRepresentativeName(applicationEntity.getRepresentativeName());
        sportsClub.setRepresentativeTelno(applicationEntity.getRepresentativeTelno());
        sportsClub.setBusinessNo(applicationEntity.getBusinessNo());
        sportsClub.setClubRoleCodeId(applicationEntity.getClubRoleCodeId());
        sportsClubMapper.insertClub(sportsClub);

        List<Long> operatingSportCodeIds =
                regSportsClubApplicationMapper.findCategoryIdsByApplicationId(applicationEntity.getId());
        if (operatingSportCodeIds == null || operatingSportCodeIds.isEmpty()) {
            operatingSportCodeIds = new ArrayList<>();
            if (applicationEntity.getOperatingSportParentCodeId() != null) {
                operatingSportCodeIds.add(applicationEntity.getOperatingSportParentCodeId());
            }
            if (applicationEntity.getOperatingSportChildCodeId() != null
                    && !applicationEntity.getOperatingSportChildCodeId().equals(applicationEntity.getOperatingSportParentCodeId())) {
                operatingSportCodeIds.add(applicationEntity.getOperatingSportChildCodeId());
            }
            saveApplicationCategories(applicationEntity.getId(), operatingSportCodeIds,
                    applicationEntity.getOperatingSportParentCodeId(),
                    applicationEntity.getOperatingSportChildCodeId());
        }

        if (!operatingSportCodeIds.isEmpty()) {
            List<SportsClubCategoryEntity> items = new ArrayList<>();
            for (Long categoryId : operatingSportCodeIds) {
                items.add(new SportsClubCategoryEntity(sportsClub.getId(), categoryId));
            }
            sportsClubMapper.insertCategories(items);
        }

        regSportsClubApplicationMapper.updateApprovedClubId(applyId, sportsClub.getId());
    }

    private void saveApplicationCategories(Long applicationId, List<Long> categoryIds,
                                           Long operatingSportParentCodeId, Long operatingSportChildCodeId) {
        regSportsClubApplicationMapper.deleteApplicationCategoriesByApplicationId(applicationId);
        List<Long> items = new ArrayList<>();
        if (categoryIds != null && !categoryIds.isEmpty()) {
            items.addAll(categoryIds);
        } else {
            if (operatingSportParentCodeId != null) {
                items.add(operatingSportParentCodeId);
            }
            if (operatingSportChildCodeId != null
                    && !operatingSportChildCodeId.equals(operatingSportParentCodeId)) {
                items.add(operatingSportChildCodeId);
            }
        }
        if (items.isEmpty()) {
            return;
        }
        regSportsClubApplicationMapper.insertApplicationCategories(applicationId, items);
    }
}
