package com.service.demo.domain.user.service;

import com.service.demo.common.error.ErrorCode;
import com.service.demo.common.exception.ApiException;
import com.service.demo.domain.user.dto.LoginRequest;
import com.service.demo.domain.user.dto.LoginResponse;
import com.service.demo.domain.user.dto.RegisterRequest;
import com.service.demo.domain.user.dto.ResetPasswordRequest;
import com.service.demo.domain.user.entity.UserEntity;
import com.service.demo.domain.user.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public LoginResponse register(RegisterRequest req) {
        UserEntity existing = userMapper.findByLoginId(req.getLoginId());
        if (existing != null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "Login ID already exists");
        }

        UserEntity user = new UserEntity();
        user.setLoginId(req.getLoginId());
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setTelno(req.getTelno());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));

        userMapper.insert(user);
        return new LoginResponse(user.getId(), user.getLoginId(), user.getUsername(), user.getEmail(), user.getTelno());
    }

    @Transactional
    public LoginResponse login(LoginRequest req) {
        UserEntity user = userMapper.findByLoginId(req.getLoginId());
        if (user == null) {
            throw new ApiException(ErrorCode.UNAUTHORIZED, "Invalid credentials");
        }

        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new ApiException(ErrorCode.UNAUTHORIZED, "Invalid credentials");
        }

        userMapper.updateLastLogin(user.getId());
        return new LoginResponse(user.getId(), user.getLoginId(), user.getUsername(), user.getEmail(), user.getTelno());
    }

    @Transactional
    public void resetPassword(Long userId, ResetPasswordRequest req) {
        UserEntity user = userMapper.findById(userId);
        if (user == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "User not found");
        }

        if (!passwordEncoder.matches(req.getCurrentPassword(), user.getPasswordHash())) {
            throw new ApiException(ErrorCode.UNAUTHORIZED, "Current password mismatch");
        }

        userMapper.updatePassword(userId, passwordEncoder.encode(req.getNewPassword()));
    }
}
