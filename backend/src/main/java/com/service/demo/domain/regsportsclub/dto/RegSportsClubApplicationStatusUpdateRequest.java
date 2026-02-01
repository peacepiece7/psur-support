package com.service.demo.domain.regsportsclub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegSportsClubApplicationStatusUpdateRequest {
    @Schema(description = "Application status code ID", example = "2")
    @NotNull
    private Long statusCodeId;

    @Schema(description = "Handler name", example = "Lee Manager")
    private String handlerName;

    @Schema(description = "Handler phone number", example = "010-5555-6666")
    private String handlerTelno;

    @Schema(description = "Handler email", example = "manager@example.com")
    private String handlerEmail;

    @Schema(description = "Handling memo", example = "Checked documents.")
    private String memo;

}
