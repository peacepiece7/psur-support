package com.service.demo.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateProfileRequest {
    @Schema(description = "User name", example = "Demo User")
    @NotBlank
    @Size(min = 2, max = 100)
    private String username;

    @Schema(description = "Email address", example = "demo@example.com")
    private String email;
    @Schema(description = "Phone number", example = "010-1234-5678")
    private String telno;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelno() {
        return telno;
    }

    public void setTelno(String telno) {
        this.telno = telno;
    }
}
