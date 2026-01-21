package com.service.demo.domain.sportsclub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class SportsClubCreateRequest {
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
    @Schema(description = "Sports category IDs", example = "[1,2]")
    private List<Long> categoryIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRepresentativeName() {
        return representativeName;
    }

    public void setRepresentativeName(String representativeName) {
        this.representativeName = representativeName;
    }

    public String getRepresentativeTelno() {
        return representativeTelno;
    }

    public void setRepresentativeTelno(String representativeTelno) {
        this.representativeTelno = representativeTelno;
    }

    public String getClubNo() {
        return clubNo;
    }

    public void setClubNo(String clubNo) {
        this.clubNo = clubNo;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public Long getClubRoleCodeId() {
        return clubRoleCodeId;
    }

    public void setClubRoleCodeId(Long clubRoleCodeId) {
        this.clubRoleCodeId = clubRoleCodeId;
    }

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }
}
