package com.service.demo.domain.role.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRoleAssignRequest {
    @Schema(description = "User ID", example = "10")
    @NotNull
    private Long userId;

    @Schema(description = "Role ID", example = "2")
    @NotNull
    private Long roleId;
}
