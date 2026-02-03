package com.service.demo.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UserRoleSummary {
    @Schema(description = "Role ID", example = "2")
    private final Long roleId;
    @Schema(description = "Role code", example = "ADMIN_SYSTEM_MANAGER")
    private final String roleCode;
    @Schema(description = "Role name", example = "System Manager")
    private final String roleName;
    @Schema(description = "Role type", example = "ACCESS")
    private final String roleType;

    public UserRoleSummary(Long roleId, String roleCode, String roleName, String roleType) {
        this.roleId = roleId;
        this.roleCode = roleCode;
        this.roleName = roleName;
        this.roleType = roleType;
    }
}
