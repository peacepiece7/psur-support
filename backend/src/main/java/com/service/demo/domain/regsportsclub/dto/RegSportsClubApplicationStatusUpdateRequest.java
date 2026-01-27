package com.service.demo.domain.regsportsclub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

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

    public Long getStatusCodeId() {
        return statusCodeId;
    }

    public void setStatusCodeId(Long statusCodeId) {
        this.statusCodeId = statusCodeId;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    public String getHandlerTelno() {
        return handlerTelno;
    }

    public void setHandlerTelno(String handlerTelno) {
        this.handlerTelno = handlerTelno;
    }

    public String getHandlerEmail() {
        return handlerEmail;
    }

    public void setHandlerEmail(String handlerEmail) {
        this.handlerEmail = handlerEmail;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
