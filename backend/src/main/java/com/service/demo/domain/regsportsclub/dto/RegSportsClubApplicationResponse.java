package com.service.demo.domain.regsportsclub.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegSportsClubApplicationResponse {
    @Schema(description = "Application info ID", example = "1")
    private Long applyId;

    @Schema(description = "Application detail ID", example = "10")
    private Long applicationId;

    @Schema(description = "Application status code", example = "APPLY")
    private String code;

    @Schema(description = "Application status code name", example = "신청")
    private String codeName;

    @Schema(description = "Applied at", example = "2025-01-01T10:00:00")
    private LocalDateTime appliedAt;

    @Schema(description = "Applicant name", example = "Kim Applicant")
    private String applicantName;

    @Schema(description = "Applicant phone number", example = "010-1111-2222")
    private String applicantTelno;

    @Schema(description = "Applicant email", example = "applicant@example.com")
    private String applicantEmail;

    @Schema(description = "Club name", example = "Seoul Sports Club")
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

    @Schema(description = "Approved sports club ID", example = "99")
    private Long approvedClubId;

    public RegSportsClubApplicationResponse() {
    }

    public RegSportsClubApplicationResponse(Long applyId, Long applicationId, String code, String codeName,
                                            LocalDateTime appliedAt, String applicantName, String applicantTelno,
                                            String applicantEmail, String clubName, String location,
                                            String representativeName, String representativeTelno, String businessNo,
                                            Long clubRoleCodeId, Long operatingSportParentCodeId,
                                            Long operatingSportChildCodeId, Long approvedClubId) {
        this.applyId = applyId;
        this.applicationId = applicationId;
        this.code = code;
        this.codeName = codeName;
        this.appliedAt = appliedAt;
        this.applicantName = applicantName;
        this.applicantTelno = applicantTelno;
        this.applicantEmail = applicantEmail;
        this.clubName = clubName;
        this.location = location;
        this.representativeName = representativeName;
        this.representativeTelno = representativeTelno;
        this.businessNo = businessNo;
        this.clubRoleCodeId = clubRoleCodeId;
        this.operatingSportParentCodeId = operatingSportParentCodeId;
        this.operatingSportChildCodeId = operatingSportChildCodeId;
        this.approvedClubId = approvedClubId;
    }

}
