package com.service.demo.domain.commoncode.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

}
