package com.service.demo.domain.regsportsclub.service;

import com.service.demo.common.error.ErrorCode;
import com.service.demo.common.exception.ApiException;
import com.service.demo.domain.commoncode.entity.CommonCodeEntity;
import com.service.demo.domain.commoncode.mapper.CommonCodeMapper;
import com.service.demo.domain.regsportsclub.dto.RegSportsClubApplicationCreateRequest;
import com.service.demo.domain.regsportsclub.dto.RegSportsClubApplicationResponse;
import com.service.demo.domain.regsportsclub.dto.RegSportsClubApplicationStatusUpdateRequest;
import com.service.demo.domain.regsportsclub.entity.RegSportsClubApplyEntity;
import com.service.demo.domain.regsportsclub.entity.RegSportsClubApplyHistoryEntity;
import com.service.demo.domain.regsportsclub.entity.RegSportsClubApplicationCategoryEntity;
import com.service.demo.domain.regsportsclub.entity.RegSportsClubApplicationEntity;
import com.service.demo.domain.regsportsclub.mapper.RegSportsClubApplicationMapper;
import com.service.demo.domain.sportsclub.entity.SportsClubCategoryEntity;
import com.service.demo.domain.sportsclub.entity.SportsClubEntity;
import com.service.demo.domain.sportsclub.mapper.SportsClubMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegSportsClubApplicationService {
    private final RegSportsClubApplicationMapper regSportsClubApplicationMapper;
    private final SportsClubMapper sportsClubMapper;
    private final CommonCodeMapper commonCodeMapper;

    public RegSportsClubApplicationService(RegSportsClubApplicationMapper regSportsClubApplicationMapper,
                                           SportsClubMapper sportsClubMapper,
                                           CommonCodeMapper commonCodeMapper) {
        this.regSportsClubApplicationMapper = regSportsClubApplicationMapper;
        this.sportsClubMapper = sportsClubMapper;
        this.commonCodeMapper = commonCodeMapper;
    }

    @Transactional
    public RegSportsClubApplicationResponse create(RegSportsClubApplicationCreateRequest req) {
        RegSportsClubApplyEntity applyEntity = new RegSportsClubApplyEntity();
        applyEntity.setStatusCodeId(req.getStatusCodeId());
        applyEntity.setAppliedAt(LocalDateTime.now());
        applyEntity.setApplicantName(req.getApplicantName());
        applyEntity.setApplicantTelno(req.getApplicantTelno());
        applyEntity.setApplicantEmail(req.getApplicantEmail());
        regSportsClubApplicationMapper.insertApply(applyEntity);

        Long clubRoleCodeId = req.getClubRoleCodeId();
        if (clubRoleCodeId == null) {
            clubRoleCodeId = commonCodeMapper.findCodeIdByGroupCodeAndCode("CLUB_ROLE", "REG_CLUB");
            if (clubRoleCodeId == null) {
                throw new ApiException(ErrorCode.BAD_REQUEST, "Club role code not found");
            }
        }

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

        return getByApplyId(applyEntity.getId());
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
            Map<Long, List<Long>> grouped = new HashMap<>();
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

    @Transactional
    public RegSportsClubApplicationResponse updateStatus(Long applyId, RegSportsClubApplicationStatusUpdateRequest req) {
        RegSportsClubApplyEntity applyEntity = regSportsClubApplicationMapper.findApplyById(applyId);
        if (applyEntity == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "Application not found");
        }

        regSportsClubApplicationMapper.updateApplyStatus(applyId, req.getStatusCodeId());

        RegSportsClubApplyHistoryEntity historyEntity = new RegSportsClubApplyHistoryEntity();
        historyEntity.setApplyId(applyId);
        historyEntity.setStatusCodeId(req.getStatusCodeId());
        historyEntity.setHandlerName(req.getHandlerName());
        historyEntity.setHandlerTelno(req.getHandlerTelno());
        historyEntity.setHandlerEmail(req.getHandlerEmail());
        historyEntity.setMemo(req.getMemo());
        historyEntity.setProcessedAt(LocalDateTime.now());
        regSportsClubApplicationMapper.insertHistory(historyEntity);

        CommonCodeEntity statusCode = commonCodeMapper.findCodeById(req.getStatusCodeId());
        if (statusCode != null && "APPROVED".equals(statusCode.getCode())) {
            approveToSportsClub(applyId);
        }

        return getByApplyId(applyId);
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
