package com.service.demo.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    @Schema(description = "User login ID", example = "demo-user")
    @NotBlank
    @Size(min = 4, max = 50)
    private String loginId;

    @Schema(description = "User name", example = "Demo User")
    @NotBlank
    @Size(min = 2, max = 100)
    private String username;

    @Schema(description = "User password", example = "P@ssw0rd123")
    @NotBlank
    @Size(min = 8, max = 100)
    private String password;

    @Schema(description = "Email address", example = "demo@example.com")
    private String email;
    @Schema(description = "Phone number", example = "010-1234-5678")
    private String telno;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
