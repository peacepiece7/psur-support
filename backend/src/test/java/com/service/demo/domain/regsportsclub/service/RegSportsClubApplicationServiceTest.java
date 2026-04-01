package com.service.demo.domain.regsportsclub.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.demo.common.constant.SessionConst;
import com.service.demo.common.exception.ApiException;
import com.service.demo.domain.commoncode.entity.CommonCodeEntity;
import com.service.demo.domain.commoncode.mapper.CommonCodeMapper;
import com.service.demo.domain.commoncode.service.CommonCodeLookupService;
import com.service.demo.domain.regsportsclub.dto.RegSportsClubApplicationResponse;
import com.service.demo.domain.regsportsclub.dto.RegSportsClubApplicationUpsertRequest;
import com.service.demo.domain.regsportsclub.entity.ApplicationActionLogEntity;
import com.service.demo.domain.regsportsclub.entity.RegSportsClubApplyEntity;
import com.service.demo.domain.regsportsclub.entity.RegSportsClubApplicationEntity;
import com.service.demo.domain.regsportsclub.mapper.ApplicationActionLogMapper;
import com.service.demo.domain.regsportsclub.mapper.RegSportsClubApplicationMapper;
import com.service.demo.domain.role.entity.UserRoleEntity;
import com.service.demo.domain.role.mapper.UserRoleMapper;
import com.service.demo.domain.sportsclub.entity.SportsClubCategoryEntity;
import com.service.demo.domain.sportsclub.entity.SportsClubEntity;
import com.service.demo.domain.sportsclub.mapper.SportsClubMapper;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@ExtendWith(MockitoExtension.class)
class RegSportsClubApplicationServiceTest {

    @Mock
    private RegSportsClubApplicationMapper regSportsClubApplicationMapper;

    @Mock
    private SportsClubMapper sportsClubMapper;

    @Mock
    private CommonCodeMapper commonCodeMapper;

    @Mock
    private CommonCodeLookupService commonCodeLookupService;

    @Mock
    private ActionService actionService;

    @Mock
    private ApplicationActionLogMapper applicationActionLogMapper;

    @Mock
    private UserRoleMapper userRoleMapper;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private RegSportsClubApplicationService regSportsClubApplicationService;

    @AfterEach
    void clearRequestContext() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void saveCreatesNewApplicationAndDelegatesSaveAction() {
        RegSportsClubApplicationUpsertRequest request = createUpsertRequest();
        request.setStatusCode("SAVED");
        request.setOperatingSportCodeIds(List.of(101L, 102L));

        mockInsertGeneratedIds();
        when(commonCodeLookupService.getCodeId("REG_SPORTS_CLUB_APPLY_STATUS", "SAVED")).thenReturn(11L);
        when(commonCodeMapper.findCodeIdByGroupCodeAndCode("CLUB_ROLE", "REG_CLUB")).thenReturn(21L);
        when(regSportsClubApplicationMapper.findApplicationDetail(1001L)).thenReturn(createResponse(1001L, 2002L, "SAVED"));
        when(regSportsClubApplicationMapper.findCategoryIdsByApplicationId(2002L)).thenReturn(List.of(101L, 102L));

        RegSportsClubApplicationResponse response = regSportsClubApplicationService.save(request);

        assertNotNull(response);
        assertEquals(1001L, response.getApplyId());
        assertEquals(List.of(101L, 102L), response.getOperatingSportCodeIds());

        ArgumentCaptor<RegSportsClubApplyEntity> applyCaptor = ArgumentCaptor.forClass(RegSportsClubApplyEntity.class);
        ArgumentCaptor<RegSportsClubApplicationEntity> applicationCaptor = ArgumentCaptor.forClass(RegSportsClubApplicationEntity.class);
        verify(regSportsClubApplicationMapper).insertApply(applyCaptor.capture());
        verify(regSportsClubApplicationMapper).insertApplication(applicationCaptor.capture());
        verify(regSportsClubApplicationMapper).insertApplicationCategories(2002L, List.of(101L, 102L));
        verify(actionService).handleAction(1001L, com.service.demo.domain.regsportsclub.constant.Action.SAVE);

        assertEquals("신청자", applyCaptor.getValue().getApplicantName());
        assertEquals("클럽", applicationCaptor.getValue().getName());
        assertEquals(21L, applicationCaptor.getValue().getClubRoleCodeId());
    }

    @Test
    void applyUpdatesExistingApplicationAndDelegatesApplyAction() {
        RegSportsClubApplicationUpsertRequest request = createUpsertRequest();
        request.setApplyId(77L);
        request.setStatusCode("APPLY");

        RegSportsClubApplyEntity existingApply = new RegSportsClubApplyEntity();
        existingApply.setId(77L);
        RegSportsClubApplicationEntity existingApplication = new RegSportsClubApplicationEntity();
        existingApplication.setId(88L);
        existingApplication.setApplyId(77L);

        when(commonCodeLookupService.getCodeId("REG_SPORTS_CLUB_APPLY_STATUS", "APPLY")).thenReturn(31L);
        when(commonCodeMapper.findCodeIdByGroupCodeAndCode("CLUB_ROLE", "REG_CLUB")).thenReturn(41L);
        when(regSportsClubApplicationMapper.findApplyById(77L)).thenReturn(existingApply);
        when(regSportsClubApplicationMapper.findApplicationByApplyId(77L)).thenReturn(existingApplication);
        when(regSportsClubApplicationMapper.findApplicationDetail(77L)).thenReturn(createResponse(77L, 88L, "APPLY"));
        when(regSportsClubApplicationMapper.findCategoryIdsByApplicationId(88L)).thenReturn(List.of(501L, 502L));

        RegSportsClubApplicationResponse response = regSportsClubApplicationService.apply(request);

        assertEquals(77L, response.getApplyId());
        verify(regSportsClubApplicationMapper).updateApply(existingApply);
        verify(regSportsClubApplicationMapper).updateApplication(existingApplication);
        verify(regSportsClubApplicationMapper).insertApplicationCategories(88L, List.of(501L, 502L));
        verify(actionService).handleAction(77L, com.service.demo.domain.regsportsclub.constant.Action.APPLY);
    }

    @Test
    void handleActionBuildsVariablesFromRolesAndStoresActionLog() {
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setAttribute(SessionConst.USER_ID, 9L);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(servletRequest));

        ReflectionTestUtils.setField(regSportsClubApplicationService, "processDefinitionId", "registration_sports_club_process");

        UserRoleEntity reviewer = new UserRoleEntity();
        reviewer.setRoleCode("REVIEWER");
        UserRoleEntity admin = new UserRoleEntity();
        admin.setRoleCode("ADMIN_SYSTEM_MANAGER");
        RegSportsClubApplyEntity applyEntity = new RegSportsClubApplyEntity();
        applyEntity.setId(123L);
        applyEntity.setStatusCodeId(44L);
        when(userRoleMapper.findAllWithRole(9L, false)).thenReturn(List.of(reviewer, admin));
        when(regSportsClubApplicationMapper.findApplyById(123L)).thenReturn(applyEntity);
        when(commonCodeLookupService.getCodeId("REG_SPORTS_CLUB_APPLY_STATUS", "REVIEW")).thenReturn(55L);
        when(actionService.handleAction(eq(123L), eq("review"), any())).thenReturn("task-999");

        regSportsClubApplicationService.handleAction(123L, "review", Map.of("note", "looks good"));

        ArgumentCaptor<Map<String, Object>> variablesCaptor = ArgumentCaptor.forClass(Map.class);
        verify(actionService).handleAction(eq(123L), eq("review"), variablesCaptor.capture());
        assertEquals("review", variablesCaptor.getValue().get("action"));
        assertEquals(List.of("REVIEWER", "ADMIN_SYSTEM_MANAGER"), variablesCaptor.getValue().get("roles"));
        assertEquals(Map.of("note", "looks good"), variablesCaptor.getValue().get("payload"));

        ArgumentCaptor<ApplicationActionLogEntity> logCaptor = ArgumentCaptor.forClass(ApplicationActionLogEntity.class);
        verify(applicationActionLogMapper).insert(logCaptor.capture());
        assertEquals(123L, logCaptor.getValue().getApplyId());
        assertEquals("review", logCaptor.getValue().getActionKey());
        assertEquals("task-999", logCaptor.getValue().getTaskKey());
        assertEquals(9L, logCaptor.getValue().getActorId());
        assertEquals("REVIEWER,ADMIN_SYSTEM_MANAGER", logCaptor.getValue().getActorRole());
        assertEquals("{\"note\":\"looks good\"}", logCaptor.getValue().getPayloadJson());
        assertEquals(55L, applyEntity.getStatusCodeId());
        verify(regSportsClubApplicationMapper).updateApply(applyEntity);
    }

    @Test
    void handleActionApproveSyncsApprovedStatusAndCreatesSportsClub() {
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setAttribute(SessionConst.USER_ID, 7L);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(servletRequest));

        ReflectionTestUtils.setField(regSportsClubApplicationService, "processDefinitionId", "registration_sports_club_process");

        UserRoleEntity approver = new UserRoleEntity();
        approver.setRoleCode("APPROVER");

        RegSportsClubApplyEntity applyEntity = new RegSportsClubApplyEntity();
        applyEntity.setId(321L);
        applyEntity.setStatusCodeId(77L);

        RegSportsClubApplicationEntity applicationEntity = new RegSportsClubApplicationEntity();
        applicationEntity.setId(654L);
        applicationEntity.setApplyId(321L);
        applicationEntity.setName("승인 클럽");
        applicationEntity.setLocation("서울");
        applicationEntity.setRepresentativeName("대표");
        applicationEntity.setRepresentativeTelno("010-2222-3333");
        applicationEntity.setBusinessNo("123-45-67890");
        applicationEntity.setClubRoleCodeId(99L);

        when(userRoleMapper.findAllWithRole(7L, false)).thenReturn(List.of(approver));
        when(regSportsClubApplicationMapper.findApplyById(321L)).thenReturn(applyEntity);
        when(commonCodeLookupService.getCodeId("REG_SPORTS_CLUB_APPLY_STATUS", "APPROVED")).thenReturn(88L);
        when(actionService.handleAction(eq(321L), eq("approve"), any())).thenReturn("task-approve");
        when(regSportsClubApplicationMapper.findApplicationByApplyId(321L)).thenReturn(applicationEntity);
        when(regSportsClubApplicationMapper.findCategoryIdsByApplicationId(654L)).thenReturn(List.of(701L, 702L));
        doAnswer(invocation -> {
            SportsClubEntity entity = invocation.getArgument(0);
            entity.setId(987L);
            return 1;
        }).when(sportsClubMapper).insertClub(any(SportsClubEntity.class));

        regSportsClubApplicationService.handleAction(321L, "approve", Map.of("note", "approved"));

        assertEquals(88L, applyEntity.getStatusCodeId());
        verify(regSportsClubApplicationMapper).updateApply(applyEntity);
        verify(sportsClubMapper).insertClub(any(SportsClubEntity.class));
        ArgumentCaptor<List<SportsClubCategoryEntity>> categoryCaptor = ArgumentCaptor.forClass(List.class);
        verify(sportsClubMapper).insertCategories(categoryCaptor.capture());
        assertEquals(2, categoryCaptor.getValue().size());
        assertEquals(987L, categoryCaptor.getValue().get(0).getClubId());
        assertEquals(701L, categoryCaptor.getValue().get(0).getCategoryId());
        assertEquals(987L, categoryCaptor.getValue().get(1).getClubId());
        assertEquals(702L, categoryCaptor.getValue().get(1).getCategoryId());
        verify(regSportsClubApplicationMapper).updateApprovedClubId(321L, 987L);
    }

    @Test
    void handleActionRejectMapsRejectedStatusFromCurrentStep() {
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setAttribute(SessionConst.USER_ID, 12L);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(servletRequest));

        ReflectionTestUtils.setField(regSportsClubApplicationService, "processDefinitionId", "registration_sports_club_process");

        UserRoleEntity reviewer = new UserRoleEntity();
        reviewer.setRoleCode("REVIEWER");

        RegSportsClubApplyEntity applyEntity = new RegSportsClubApplyEntity();
        applyEntity.setId(432L);
        applyEntity.setStatusCodeId(66L);

        CommonCodeEntity currentStatus = new CommonCodeEntity();
        currentStatus.setCode("RECEIVED");

        when(userRoleMapper.findAllWithRole(12L, false)).thenReturn(List.of(reviewer));
        when(regSportsClubApplicationMapper.findApplyById(432L)).thenReturn(applyEntity);
        when(commonCodeMapper.findCodeById(66L)).thenReturn(currentStatus);
        when(commonCodeLookupService.getCodeId("REG_SPORTS_CLUB_APPLY_STATUS", "REVIEW_REJECTED")).thenReturn(67L);
        when(actionService.handleAction(eq(432L), eq("reject"), any())).thenReturn("task-reject");

        regSportsClubApplicationService.handleAction(432L, "reject", Map.of("note", "rejected"));

        assertEquals(67L, applyEntity.getStatusCodeId());
        verify(regSportsClubApplicationMapper).updateApply(applyEntity);
    }

    @Test
    void handleActionThrowsWhenRequestContextDoesNotHaveUser() {
        RequestContextHolder.resetRequestAttributes();

        assertThrows(
                ApiException.class,
                () -> regSportsClubApplicationService.handleAction(1L, "approve", Map.of())
        );

        verify(actionService, never()).handleAction(eq(1L), eq("approve"), any());
    }

    private void mockInsertGeneratedIds() {
        doAnswer(invocation -> {
            RegSportsClubApplyEntity entity = invocation.getArgument(0);
            entity.setId(1001L);
            return 1;
        }).when(regSportsClubApplicationMapper).insertApply(any(RegSportsClubApplyEntity.class));

        doAnswer(invocation -> {
            RegSportsClubApplicationEntity entity = invocation.getArgument(0);
            entity.setId(2002L);
            return 1;
        }).when(regSportsClubApplicationMapper).insertApplication(any(RegSportsClubApplicationEntity.class));
    }

    private RegSportsClubApplicationUpsertRequest createUpsertRequest() {
        RegSportsClubApplicationUpsertRequest request = new RegSportsClubApplicationUpsertRequest();
        request.setApplicantName("신청자");
        request.setApplicantTelno("010-1111-2222");
        request.setApplicantEmail("apply@example.com");
        request.setClubName("클럽");
        request.setLocation("서울");
        request.setRepresentativeName("대표");
        request.setRepresentativeTelno("010-3333-4444");
        request.setBusinessNo("123-45-67890");
        request.setOperatingSportParentCodeId(501L);
        request.setOperatingSportChildCodeId(502L);
        return request;
    }

    private RegSportsClubApplicationResponse createResponse(Long applyId, Long applicationId, String code) {
        RegSportsClubApplicationResponse response = new RegSportsClubApplicationResponse();
        response.setApplyId(applyId);
        response.setApplicationId(applicationId);
        response.setCode(code);
        return response;
    }
}
