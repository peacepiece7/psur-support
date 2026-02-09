package com.service.demo.domain.regsportsclub.service;

import com.service.demo.common.error.ErrorCode;
import com.service.demo.common.exception.ApiException;
import com.service.demo.domain.regsportsclub.constant.Action;
import com.service.demo.domain.regsportsclub.entity.RegSportsClubApplyEntity;
import com.service.demo.domain.regsportsclub.mapper.RegSportsClubApplicationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActionService {
    private final RegSportsClubApplicationMapper regSportsClubApplicationMapper;
    private final BpmService bpmService;

    public ActionService(RegSportsClubApplicationMapper regSportsClubApplicationMapper, BpmService bpmService) {
        this.regSportsClubApplicationMapper = regSportsClubApplicationMapper;
        this.bpmService = bpmService;
    }

    @Transactional
    public void handleAction(Long applyId, Action action) {
        RegSportsClubApplyEntity applyEntity = regSportsClubApplicationMapper.findApplyById(applyId);
        if (applyEntity == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "Application not found");
        }
        ensureProcessStarted(applyEntity);
        if (action == Action.SAVE) {
            return;
        }

        bpmService.update(applyEntity.getProcessInstanceId(), action.getBpmValue());
    }

    private void ensureProcessStarted(RegSportsClubApplyEntity applyEntity) {
        if (applyEntity.getProcessInstanceId() != null) {
            return;
        }
        String processInstanceId = bpmService.start();
        regSportsClubApplicationMapper.updateApplyProcessInstanceId(applyEntity.getId(), processInstanceId);
        applyEntity.setProcessInstanceId(processInstanceId);
    }
}
