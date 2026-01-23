package com.service.demo.domain.commoncode.service;

import com.service.demo.common.error.ErrorCode;
import com.service.demo.common.exception.ApiException;
import com.service.demo.domain.commoncode.dto.CommonCodeGroupResponse;
import com.service.demo.domain.commoncode.dto.CommonCodeGroupSummaryResponse;
import com.service.demo.domain.commoncode.dto.CommonCodeResponse;
import com.service.demo.domain.commoncode.entity.CommonCodeEntity;
import com.service.demo.domain.commoncode.entity.CommonCodeGroupEntity;
import com.service.demo.domain.commoncode.mapper.CommonCodeMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommonCodeService {
    private final CommonCodeMapper commonCodeMapper;

    public CommonCodeService(CommonCodeMapper commonCodeMapper) {
        this.commonCodeMapper = commonCodeMapper;
    }

    public CommonCodeGroupResponse getGroupTree(String groupCode) {
        CommonCodeGroupEntity root = commonCodeMapper.findGroupByCode(groupCode);
        if (root == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "Group not found");
        }

        List<CommonCodeGroupEntity> level2 = commonCodeMapper.findGroupsByParentId(root.getId());
        List<CommonCodeGroupEntity> level3 = new ArrayList<>();
        if (!level2.isEmpty()) {
            List<Long> parentIds = new ArrayList<>();
            for (CommonCodeGroupEntity g : level2) {
                parentIds.add(g.getId());
            }
            level3 = commonCodeMapper.findGroupsByParentIds(parentIds);
        }

        List<CommonCodeGroupEntity> allGroups = new ArrayList<>();
        allGroups.add(root);
        allGroups.addAll(level2);
        allGroups.addAll(level3);

        List<Long> groupIds = new ArrayList<>();
        for (CommonCodeGroupEntity g : allGroups) {
            groupIds.add(g.getId());
        }

        Map<Long, CommonCodeGroupResponse> nodeMap = new HashMap<>();
        for (CommonCodeGroupEntity g : allGroups) {
            CommonCodeGroupResponse node = new CommonCodeGroupResponse(
                    g.getGroupCode(), g.getGroupName(), g.getDescription(), g.getLevel(), g.getSortOrder()
            );
            nodeMap.put(g.getId(), node);
        }

        if (!groupIds.isEmpty()) {
            List<CommonCodeEntity> codes = commonCodeMapper.findCodesByGroupIds(groupIds);
            for (CommonCodeEntity code : codes) {
                CommonCodeGroupResponse parent = nodeMap.get(code.getGroupId());
                if (parent != null) {
                    parent.getCodes().add(new CommonCodeResponse(
                            code.getCode(), code.getCodeName(), code.getDescription(), code.getSortOrder()
                    ));
                }
            }
        }

        for (CommonCodeGroupEntity g : level2) {
            CommonCodeGroupResponse parent = nodeMap.get(root.getId());
            CommonCodeGroupResponse child = nodeMap.get(g.getId());
            if (parent != null && child != null) {
                parent.getChildren().add(child);
            }
        }

        for (CommonCodeGroupEntity g : level3) {
            CommonCodeGroupResponse parent = nodeMap.get(g.getParentGroupId());
            CommonCodeGroupResponse child = nodeMap.get(g.getId());
            if (parent != null && child != null) {
                parent.getChildren().add(child);
            }
        }

        return nodeMap.get(root.getId());
    }

    public List<CommonCodeGroupSummaryResponse> listGroups() {
        List<CommonCodeGroupEntity> groups = commonCodeMapper.findAllGroups();
        List<CommonCodeGroupSummaryResponse> result = new ArrayList<>();
        for (CommonCodeGroupEntity g : groups) {
            result.add(new CommonCodeGroupSummaryResponse(
                    g.getId(),
                    g.getGroupCode(),
                    g.getGroupName(),
                    g.getParentGroupId(),
                    g.getLevel(),
                    g.getSortOrder(),
                    g.getDescription()
            ));
        }
        return result;
    }
}
