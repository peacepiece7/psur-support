package com.service.demo.domain.role.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class RoleResponse {
    @Schema(description = "Role ID", example = "1")
    private final Long id;
    @Schema(description = "Role code", example = "ADMIN_SYSTEM_MANAGER")
    private final String roleCode;
    @Schema(description = "Role name", example = "System Manager")
    private final String roleName;
    @Schema(description = "Role type", example = "ACCESS")
    private final String roleType;
    @Schema(description = "Role description", example = "System admin role")
    private final String description;
    @Schema(description = "Role active flag", example = "true")
    private final Boolean isActive;

    public RoleResponse(Long id, String roleCode, String roleName, String roleType,
                        String description, Boolean isActive) {
        this.id = id;
        this.roleCode = roleCode;
        this.roleName = roleName;
        this.roleType = roleType;
        this.description = description;
        this.isActive = isActive;
    }
}
