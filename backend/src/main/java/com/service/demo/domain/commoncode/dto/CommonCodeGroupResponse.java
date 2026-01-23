package com.service.demo.domain.commoncode.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

public class CommonCodeGroupResponse {
    @Schema(description = "Group code", example = "CLUB_ROLE")
    private final String groupCode;
    @Schema(description = "Group name", example = "Sports Club Role")
    private final String groupName;
    @Schema(description = "Group description", example = "Roles available for sports clubs")
    private final String description;
    @Schema(description = "Group level (1-3)", example = "1")
    private final Integer level;
    @Schema(description = "Sort order", example = "1")
    private final Integer sortOrder;
    @Schema(description = "Codes under this group")
    private final List<CommonCodeResponse> codes = new ArrayList<>();
    @Schema(description = "Child groups")
    private final List<CommonCodeGroupResponse> children = new ArrayList<>();

    public CommonCodeGroupResponse(String groupCode, String groupName, String description, Integer level, Integer sortOrder) {
        this.groupCode = groupCode;
        this.groupName = groupName;
        this.description = description;
        this.level = level;
        this.sortOrder = sortOrder;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getDescription() {
        return description;
    }

    public Integer getLevel() {
        return level;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public List<CommonCodeResponse> getCodes() {
        return codes;
    }

    public List<CommonCodeGroupResponse> getChildren() {
        return children;
    }
}
