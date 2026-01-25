package com.service.demo.domain.commoncode.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class CommonCodeCreateRequest {
    @Schema(description = "Group code", example = "REGION")
    private String groupCode;
    @Schema(description = "Code value", example = "SEOUL")
    private String code;
    @Schema(description = "Code display name", example = "Seoul")
    private String codeName;
    @Schema(description = "Child group code mapped to this code", example = "SEOUL")
    private String childGroupCode;
    @Schema(description = "Sort order", example = "1")
    private Integer sortOrder;
    @Schema(description = "Code description")
    private String description;
    @Schema(description = "Is active", example = "true")
    private Boolean isActive;

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getChildGroupCode() {
        return childGroupCode;
    }

    public void setChildGroupCode(String childGroupCode) {
        this.childGroupCode = childGroupCode;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
