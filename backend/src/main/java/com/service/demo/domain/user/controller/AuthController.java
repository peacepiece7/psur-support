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

@Tag(name = "Auth", description = "인증 및 세션 관리")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "회원가입", description = "새 사용자 계정을 생성하고 프로필을 반환합니다.")
    @PostMapping("/register")
    public ApiResponse<LoginResponse> register(@Valid @RequestBody RegisterRequest req) {
        return ApiResponse.ok(authService.register(req));
    }

    @Operation(summary = "로그인", description = "자격 증명을 확인하고 세션을 생성합니다.")
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest req,
                                            @Parameter(hidden = true) HttpSession session) {
        LoginResponse response = authService.login(req);
        session.setAttribute(SessionConst.USER_ID, response.getId());
        return ApiResponse.ok(response);
    }

    @Operation(summary = "로그아웃", description = "현재 세션을 종료합니다.")
    @PostMapping("/logout")
    public ApiResponse<Void> logout(@Parameter(hidden = true) HttpSession session) {
        session.invalidate();
        return ApiResponse.ok(null);
    }

    @Operation(summary = "비밀번호 변경", description = "로그인한 사용자의 비밀번호를 변경합니다.")
    @PostMapping("/password/reset")
    public ApiResponse<Void> resetPassword(@Parameter(hidden = true) @UserSession Long userId,
                                           @Valid @RequestBody ResetPasswordRequest req) {
        authService.resetPassword(userId, req);
        return ApiResponse.ok(null);
    }
}
