package com.service.demo.domain.role.controller;

import com.service.demo.common.api.ApiResponse;
import com.service.demo.domain.role.dto.RoleResponse;
import com.service.demo.domain.role.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Roles", description = "Role management")
@SecurityRequirement(name = "sessionAuth")
@RestController
@RequestMapping("/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @Operation(summary = "List roles", description = "Returns all roles")
    @GetMapping
    public ApiResponse<List<RoleResponse>> list(
            @Parameter(description = "Include inactive roles") Boolean includeInactive) {
        return ApiResponse.ok(roleService.list(Boolean.TRUE.equals(includeInactive)));
    }
}
