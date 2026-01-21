package com.service.demo.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class LoginResponse {
    @Schema(description = "User ID", example = "1")
    private final Long id;
    @Schema(description = "User login ID", example = "demo-user")
    private final String loginId;
    @Schema(description = "User name", example = "Demo User")
    private final String username;
    @Schema(description = "Email address", example = "demo@example.com")
    private final String email;
    @Schema(description = "Phone number", example = "010-1234-5678")
    private final String telno;

    public LoginResponse(Long id, String loginId, String username, String email, String telno) {
        this.id = id;
        this.loginId = loginId;
        this.username = username;
        this.email = email;
        this.telno = telno;
    }

    public Long getId() {
        return id;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getTelno() {
        return telno;
    }
}
