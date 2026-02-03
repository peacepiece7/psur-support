package com.service.demo.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class UserListResponse {
    @Schema(description = "User ID", example = "1")
    private final Long id;
    @Schema(description = "Login ID", example = "demo")
    private final String loginId;
    @Schema(description = "User name", example = "Demo User")
    private final String username;
    @Schema(description = "Email", example = "demo@test.com")
    private final String email;
    @Schema(description = "Phone number", example = "010-1111-2222")
    private final String telno;
    @Schema(description = "Status", example = "ACTIVE")
    private final String status;
    @Schema(description = "Active flag", example = "true")
    private final Boolean isActive;
    @Schema(description = "Last login time")
    private final LocalDateTime lastLoginAt;
    @Schema(description = "Created time")
    private final LocalDateTime createdAt;

    public UserListResponse(Long id, String loginId, String username, String email, String telno,
                            String status, Boolean isActive, LocalDateTime lastLoginAt,
                            LocalDateTime createdAt) {
        this.id = id;
        this.loginId = loginId;
        this.username = username;
        this.email = email;
        this.telno = telno;
        this.status = status;
        this.isActive = isActive;
        this.lastLoginAt = lastLoginAt;
        this.createdAt = createdAt;
    }
}
