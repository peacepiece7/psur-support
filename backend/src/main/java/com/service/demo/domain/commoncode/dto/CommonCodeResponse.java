package com.service.demo.domain.commoncode.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class CommonCodeResponse {
    @Schema(description = "Code ID", example = "123")
    private final Long id;
    @Schema(description = "Code value", example = "REGISTERED")
    private final String code;
    @Schema(description = "Code display name", example = "Registered Club")
    private final String codeName;
    @Schema(description = "Code description", example = "Club role for registered clubs")
    private final String description;
    @Schema(description = "Sort order", example = "1")
    private final Integer sortOrder;
    @Schema(description = "Child group code mapped to this code", example = "OPERATING_SPORT_LEISURE_EXTREME")
    private final String groupCode;

    public CommonCodeResponse(Long id, String code, String codeName, String description, Integer sortOrder, String groupCode) {
        this.id = id;
        this.code = code;
        this.codeName = codeName;
        this.description = description;
        this.sortOrder = sortOrder;
        this.groupCode = groupCode;
    }

}
