package com.service.demo.domain.sportsclub.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import lombok.Getter;

@Getter
public class SportsClubResponse {
    @Schema(description = "Club ID", example = "1")
    private final Long id;
    @Schema(description = "Club name", example = "Seoul Sports Club")
    private final String name;
    @Schema(description = "Club location", example = "Seoul, Korea")
    private final String location;
    @Schema(description = "Representative name", example = "Kim Coach")
    private final String representativeName;
    @Schema(description = "Representative phone number", example = "010-1111-2222")
    private final String representativeTelno;
    @Schema(description = "Club number", example = "CLUB-2024-0001")
    private final String clubNo;
    @Schema(description = "Business registration number", example = "123-45-67890")
    private final String businessNo;
    @Schema(description = "Common code ID for club role", example = "10")
    private final Long clubRoleCodeId;
    @Schema(description = "Club active flag", example = "true")
    private final Boolean isActive;
    @Schema(description = "Operating sport common code IDs", example = "[1,2]")
    private final List<Long> categoryIds;

    public SportsClubResponse(Long id, String name, String location, String representativeName,
                              String representativeTelno, String clubNo, String businessNo,
                              Long clubRoleCodeId, Boolean isActive, List<Long> categoryIds) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.representativeName = representativeName;
        this.representativeTelno = representativeTelno;
        this.clubNo = clubNo;
        this.businessNo = businessNo;
        this.clubRoleCodeId = clubRoleCodeId;
        this.isActive = isActive;
        this.categoryIds = categoryIds;
    }

}
