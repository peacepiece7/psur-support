package com.service.demo.domain.user.controller;

import com.service.demo.common.api.ApiResponse;
import com.service.demo.common.annotation.UserSession;
import com.service.demo.domain.user.dto.UpdateProfileRequest;
import com.service.demo.domain.user.dto.UserResponse;
import com.service.demo.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Users", description = "User profile endpoints")
@SecurityRequirement(name = "sessionAuth")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get my profile", description = "Returns the authenticated user's profile.")
    @GetMapping("/me")
    public ApiResponse<UserResponse> me(@Parameter(hidden = true) @UserSession Long userId) {
        return ApiResponse.ok(userService.getById(userId));
    }

    @Operation(summary = "Update my profile", description = "Updates the authenticated user's profile.")
    @PutMapping("/me")
    public ApiResponse<UserResponse> update(@Parameter(hidden = true) @UserSession Long userId,
                                            @Valid @RequestBody UpdateProfileRequest req) {
        return ApiResponse.ok(userService.updateProfile(userId, req));
    }
}
