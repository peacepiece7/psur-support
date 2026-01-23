package com.service.demo.domain.commoncode.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Common code group summary")
public class CommonCodeGroupSummaryResponse {
    @Schema(description = "Group ID", example = "1")
    private final Long id;
    @Schema(description = "Group code", example = "CLUB_ROLE")
    private final String groupCode;
    @Schema(description = "Group name", example = "Sports Club Role")
    private final String groupName;
    @Schema(description = "Parent group ID", example = "null")
    private final Long parentGroupId;
    @Schema(description = "Group level (1-3)", example = "1")
    private final Integer level;
    @Schema(description = "Sort order", example = "1")
    private final Integer sortOrder;
    @Schema(description = "Group description", example = "Roles available for sports clubs")
    private final String description;

    public CommonCodeGroupSummaryResponse(Long id, String groupCode, String groupName,
                                          Long parentGroupId, Integer level,
                                          Integer sortOrder, String description) {
        this.id = id;
        this.groupCode = groupCode;
        this.groupName = groupName;
        this.parentGroupId = parentGroupId;
        this.level = level;
        this.sortOrder = sortOrder;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public Long getParentGroupId() {
        return parentGroupId;
    }

    public Integer getLevel() {
        return level;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public String getDescription() {
        return description;
    }
}
