package com.service.demo.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @Schema(description = "User login ID", example = "demo-user")
    @NotBlank
    private String loginId;

    @Schema(description = "User password", example = "P@ssw0rd123")
    @NotBlank
    private String password;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
