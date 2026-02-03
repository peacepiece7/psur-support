package com.service.demo.domain.role.controller;

import com.service.demo.common.api.ApiResponse;
import com.service.demo.domain.role.dto.UserRoleAssignRequest;
import com.service.demo.domain.role.dto.UserRoleBulkUpdateRequest;
import com.service.demo.domain.role.dto.UserRoleResponse;
import com.service.demo.domain.role.dto.UserRoleUpdateRequest;
import com.service.demo.domain.role.service.UserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "User Roles", description = "User role assignment")
@SecurityRequirement(name = "sessionAuth")
@RestController
@RequestMapping("/user-roles")
public class UserRoleController {
    private final UserRoleService userRoleService;

    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @Operation(summary = "List user roles", description = "Returns user role assignments")
    @GetMapping
    public ApiResponse<List<UserRoleResponse>> list(
            @Parameter(description = "User ID") @RequestParam(required = false) Long userId,
            @Parameter(description = "Include inactive user roles") @RequestParam(required = false) Boolean includeInactive) {
        return ApiResponse.ok(userRoleService.list(userId, Boolean.TRUE.equals(includeInactive)));
    }

    @Operation(summary = "Assign role to user", description = "Assigns a role to a user (upsert)")
    @PostMapping
    public ApiResponse<UserRoleResponse> assign(@Valid @RequestBody UserRoleAssignRequest request) {
        return ApiResponse.ok(userRoleService.assign(request));
    }

    @Operation(summary = "Update user role", description = "Soft deletes old role and assigns new role")
    @PutMapping
    public ApiResponse<UserRoleResponse> update(@Valid @RequestBody UserRoleUpdateRequest request) {
        return ApiResponse.ok(userRoleService.update(request));
    }

    @Operation(summary = "Bulk update user roles", description = "Replaces user roles with given list")
    @PutMapping("/bulk")
    public ApiResponse<List<UserRoleResponse>> bulkUpdate(@Valid @RequestBody UserRoleBulkUpdateRequest request) {
        return ApiResponse.ok(userRoleService.bulkUpdate(request));
    }

    @Operation(summary = "Delete user role", description = "Soft deletes a user role assignment")
    @DeleteMapping
    public ApiResponse<Void> delete(
            @Parameter(description = "User ID") @RequestParam Long userId,
            @Parameter(description = "Role ID") @RequestParam Long roleId) {
        userRoleService.delete(userId, roleId);
        return ApiResponse.ok(null);
    }
}
