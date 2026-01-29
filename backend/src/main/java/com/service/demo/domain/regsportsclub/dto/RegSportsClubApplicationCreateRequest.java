package com.service.demo.domain.regsportsclub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import lombok.Getter;
public class RegSportsClubApplicationCreateRequest {
    @Schema(description = "Application status code ID", example = "1")
    private Long statusCodeId;

    @Schema(description = "Application status code", example = "APPLY")
    private String statusCode;

    @Schema(description = "Applicant name", example = "Kim Applicant")
    @NotBlank
    private String applicantName;

    @Schema(description = "Applicant phone number", example = "010-1111-2222")
    @NotBlank
    private String applicantTelno;

    @Schema(description = "Applicant email", example = "applicant@example.com")
    @NotBlank
    private String applicantEmail;

    @Schema(description = "Club name", example = "Seoul Sports Club")
    @NotBlank
    private String clubName;

    @Schema(description = "Club location", example = "Seoul, Korea")
    private String location;

    @Schema(description = "Representative name", example = "Kim Coach")
    private String representativeName;

    @Schema(description = "Representative phone number", example = "010-3333-4444")
    private String representativeTelno;

    @Schema(description = "Business registration number", example = "123-45-67890")
    private String businessNo;

    @Schema(description = "Club role code ID", example = "10")
    private Long clubRoleCodeId;

    @Schema(description = "Operating sport parent code ID", example = "100")
    private Long operatingSportParentCodeId;

    @Schema(description = "Operating sport child code ID", example = "200")
    private Long operatingSportChildCodeId;
    @Schema(description = "Operating sport common code IDs", example = "[100,200]")
    private List<Long> operatingSportCodeIds;

    public Long getStatusCodeId() {
        return statusCodeId;
    }

    public void setStatusCodeId(Long statusCodeId) {
        this.statusCodeId = statusCodeId;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicantTelno() {
        return applicantTelno;
    }

    public void setApplicantTelno(String applicantTelno) {
        this.applicantTelno = applicantTelno;
    }

    public String getApplicantEmail() {
        return applicantEmail;
    }

    public void setApplicantEmail(String applicantEmail) {
        this.applicantEmail = applicantEmail;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
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

    public Long getOperatingSportParentCodeId() {
        return operatingSportParentCodeId;
    }

    public void setOperatingSportParentCodeId(Long operatingSportParentCodeId) {
        this.operatingSportParentCodeId = operatingSportParentCodeId;
    }

    public Long getOperatingSportChildCodeId() {
        return operatingSportChildCodeId;
    }

    public void setOperatingSportChildCodeId(Long operatingSportChildCodeId) {
        this.operatingSportChildCodeId = operatingSportChildCodeId;
    }

    public List<Long> getOperatingSportCodeIds() {
        return operatingSportCodeIds;
    }

    public void setOperatingSportCodeIds(List<Long> operatingSportCodeIds) {
        this.operatingSportCodeIds = operatingSportCodeIds;
    }
}
