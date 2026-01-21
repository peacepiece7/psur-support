package com.service.demo.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetPasswordRequest {
    @Schema(description = "Current password", example = "OldP@ssw0rd")
    @NotBlank
    private String currentPassword;

    @Schema(description = "New password", example = "NewP@ssw0rd123")
    @NotBlank
    @Size(min = 8, max = 100)
    private String newPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
