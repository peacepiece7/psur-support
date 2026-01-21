package com.service.demo.domain.user.controller;

import com.service.demo.common.api.ApiResponse;
import com.service.demo.common.annotation.UserSession;
import com.service.demo.common.constant.SessionConst;
import com.service.demo.domain.user.dto.LoginRequest;
import com.service.demo.domain.user.dto.LoginResponse;
import com.service.demo.domain.user.dto.RegisterRequest;
import com.service.demo.domain.user.dto.ResetPasswordRequest;
import com.service.demo.domain.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "Authentication and session management")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Register a new user", description = "Creates a new user account and returns the profile.")
    @PostMapping("/register")
    public ApiResponse<LoginResponse> register(@Valid @RequestBody RegisterRequest req) {
        return ApiResponse.ok(authService.register(req));
    }

    @Operation(summary = "Login", description = "Validates credentials and creates a session.")
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest req, HttpSession session) {
        LoginResponse response = authService.login(req);
        session.setAttribute(SessionConst.USER_ID, response.getId());
        return ApiResponse.ok(response);
    }

    @Operation(summary = "Logout", description = "Invalidates the current session.")
    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpSession session) {
        session.invalidate();
        return ApiResponse.ok(null);
    }

    @Operation(summary = "Reset password", description = "Changes the password for the authenticated user.")
    @PostMapping("/password/reset")
    public ApiResponse<Void> resetPassword(@Parameter(hidden = true) @UserSession Long userId,
                                           @Valid @RequestBody ResetPasswordRequest req) {
        authService.resetPassword(userId, req);
        return ApiResponse.ok(null);
    }
}
