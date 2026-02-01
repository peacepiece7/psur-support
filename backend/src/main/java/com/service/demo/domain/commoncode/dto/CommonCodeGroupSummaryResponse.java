package com.service.demo.domain.commoncode.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Common code group summary")
public record CommonCodeGroupSummaryResponse(
    @Schema(description = "Group ID", example = "1") Long id,
    @Schema(description = "Group code", example = "CLUB_ROLE") String groupCode,
    @Schema(description = "Group name", example = "Sports Club Role") String groupName,
    @Schema(description = "Parent group ID", example = "null") Long parentGroupId,
    @Schema(description = "Group level (1-3)", example = "1") Integer level,
    @Schema(description = "Sort order", example = "1") Integer sortOrder,
    @Schema(description = "Group description", example = "Roles available for sports clubs") String description) {

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

    @Override
    public Long id() {
        return id;
    }

    @Override
    public String groupCode() {
        return groupCode;
    }

    @Override
    public String groupName() {
        return groupName;
    }

    @Override
    public Long parentGroupId() {
        return parentGroupId;
    }

    @Override
    public Integer level() {
        return level;
    }

    @Override
    public Integer sortOrder() {
        return sortOrder;
    }

    @Override
    public String description() {
        return description;
    }
}
