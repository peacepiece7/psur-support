package com.service.demo.domain.regsportsclub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegSportsClubApplicationUpsertRequest {
    @Schema(description = "Application info ID (optional for new save/apply)", example = "1")
    private Long applyId;

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
}
