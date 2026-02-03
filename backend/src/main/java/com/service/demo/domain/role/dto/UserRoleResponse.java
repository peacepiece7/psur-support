package com.service.demo.domain.role.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class UserRoleResponse {
    @Schema(description = "User role ID", example = "1")
    private final Long id;
    @Schema(description = "User ID", example = "10")
    private final Long userId;
    @Schema(description = "Role ID", example = "2")
    private final Long roleId;
    @Schema(description = "Role code", example = "ADMIN_SYSTEM_MANAGER")
    private final String roleCode;
    @Schema(description = "Role name", example = "System Manager")
    private final String roleName;
    @Schema(description = "Role type", example = "ACCESS")
    private final String roleType;
    @Schema(description = "User role active flag", example = "true")
    private final Boolean isActive;
    @Schema(description = "Assigned at")
    private final LocalDateTime assignedAt;
    @Schema(description = "Updated at")
    private final LocalDateTime updatedAt;
    @Schema(description = "Deleted at")
    private final LocalDateTime deletedAt;

    public UserRoleResponse(Long id, Long userId, Long roleId, String roleCode, String roleName,
                            String roleType, Boolean isActive, LocalDateTime assignedAt,
                            LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.userId = userId;
        this.roleId = roleId;
        this.roleCode = roleCode;
        this.roleName = roleName;
        this.roleType = roleType;
        this.isActive = isActive;
        this.assignedAt = assignedAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }
}
