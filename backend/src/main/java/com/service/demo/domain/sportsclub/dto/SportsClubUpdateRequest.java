package com.service.demo.domain.sportsclub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SportsClubUpdateRequest {
    @Schema(description = "Club name", example = "Seoul Sports Club")
    @NotBlank
    private String name;

    @Schema(description = "Club location", example = "Seoul, Korea")
    private String location;
    @Schema(description = "Representative name", example = "Kim Coach")
    private String representativeName;
    @Schema(description = "Representative phone number", example = "010-1111-2222")
    private String representativeTelno;
    @Schema(description = "Club number", example = "CLUB-2024-0001")
    private String clubNo;
    @Schema(description = "Business registration number", example = "123-45-67890")
    private String businessNo;
    @Schema(description = "Common code ID for club role", example = "10")
    private Long clubRoleCodeId;
    @Schema(description = "Club active flag", example = "true")
    private Boolean isActive;
    @Schema(description = "Operating sport common code IDs", example = "[1,2]")
    private List<Long> categoryIds;

}
