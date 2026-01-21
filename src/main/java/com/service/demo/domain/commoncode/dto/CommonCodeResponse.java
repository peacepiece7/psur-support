package com.service.demo.domain.commoncode.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class CommonCodeResponse {
    @Schema(description = "Code value", example = "REGISTERED")
    private final String code;
    @Schema(description = "Code display name", example = "Registered Club")
    private final String codeName;
    @Schema(description = "Code description", example = "Club role for registered clubs")
    private final String description;
    @Schema(description = "Sort order", example = "1")
    private final Integer sortOrder;

    public CommonCodeResponse(String code, String codeName, String description, Integer sortOrder) {
        this.code = code;
        this.codeName = codeName;
        this.description = description;
        this.sortOrder = sortOrder;
    }

    public String getCode() {
        return code;
    }

    public String getCodeName() {
        return codeName;
    }

    public String getDescription() {
        return description;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }
}
