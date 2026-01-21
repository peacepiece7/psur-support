package com.service.demo.domain.user.model;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserSessionInfo {
    @Schema(description = "User ID from session", example = "1")
    private final Long id;

    public UserSessionInfo(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
