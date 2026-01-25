package com.service.demo.domain.commoncode.service;

import com.service.demo.common.error.ErrorCode;
import com.service.demo.common.exception.ApiException;
import com.service.demo.domain.commoncode.dto.CommonCodeGroupListResponse;
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

    public CommonCodeGroupResponse getGroupTree(String groupCode, Integer depth, boolean includeCodes) {
        CommonCodeGroupEntity root = commonCodeMapper.findGroupByCode(groupCode);
        if (root == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "Group not found");
        }

        int maxDepth = depth == null ? 3 : Math.min(3, Math.max(1, depth));
        List<CommonCodeGroupEntity> allGroups = new ArrayList<>();
        allGroups.add(root);

        List<CommonCodeGroupEntity> currentLevel = List.of(root);
        for (int level = 2; level <= maxDepth; level++) {
            List<Long> parentIds = new ArrayList<>();
            for (CommonCodeGroupEntity g : currentLevel) {
                parentIds.add(g.getId());
            }
            if (parentIds.isEmpty()) {
                break;
            }

            List<CommonCodeGroupEntity> children = commonCodeMapper.findGroupsByParentIds(parentIds);
            if (children.isEmpty()) {
                break;
            }
            allGroups.addAll(children);
            currentLevel = children;
        }

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

        if (includeCodes && !groupIds.isEmpty()) {
            List<CommonCodeEntity> codes = commonCodeMapper.findCodesByGroupIds(groupIds);
            for (CommonCodeEntity code : codes) {
                CommonCodeGroupResponse parent = nodeMap.get(code.getGroupId());
                if (parent != null) {
                    parent.getCodes().add(new CommonCodeResponse(
                            code.getCode(),
                            code.getCodeName(),
                            code.getDescription(),
                            code.getSortOrder(),
                            code.getChildGroupCode()
                    ));
                }
            }
        }

        for (CommonCodeGroupEntity g : allGroups) {
            if (g.getParentGroupId() == null) {
                continue;
            }
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

    public CommonCodeGroupListResponse listGroups(String name,
                                                  boolean includeInactive,
                                                  String sortBy,
                                                  String sortDir,
                                                  int offset,
                                                  int limit) {
        int safeOffset = Math.max(0, offset);
        int safeLimit = limit <= 0 ? 20 : Math.min(limit, 200);
        String sortColumn = resolveSortColumn(sortBy);
        String sortDirection = resolveSortDirection(sortDir);

        int queryLimit = safeLimit + 1;
        List<CommonCodeGroupEntity> groups = commonCodeMapper.findGroupsFiltered(
                name, includeInactive, sortColumn, sortDirection, safeOffset, queryLimit
        );

        boolean hasNext = groups.size() > safeLimit;
        if (hasNext) {
            groups = groups.subList(0, safeLimit);
        }

        List<CommonCodeGroupSummaryResponse> items = new ArrayList<>();
        for (CommonCodeGroupEntity g : groups) {
            items.add(new CommonCodeGroupSummaryResponse(
                    g.getId(),
                    g.getGroupCode(),
                    g.getGroupName(),
                    g.getParentGroupId(),
                    g.getLevel(),
                    g.getSortOrder(),
                    g.getDescription()
            ));
        }

        Integer nextOffset = hasNext ? safeOffset + safeLimit : null;
        return new CommonCodeGroupListResponse(items, nextOffset, hasNext, safeLimit);
    }

    private String resolveSortColumn(String sortBy) {
        if (sortBy == null || sortBy.isBlank()) {
            return "sort_order";
        }
        return switch (sortBy) {
            case "groupCode" -> "group_code";
            case "groupName" -> "group_name";
            case "level" -> "level";
            case "sortOrder" -> "sort_order";
            case "id" -> "id";
            case "parentGroupId" -> "parent_group_id";
            default -> "sort_order";
        };
    }

    private String resolveSortDirection(String sortDir) {
        if (sortDir == null) {
            return "ASC";
        }
        return "desc".equalsIgnoreCase(sortDir) ? "DESC" : "ASC";
    }
}
