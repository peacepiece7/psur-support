package com.service.demo.domain.user.service;

import com.service.demo.common.error.ErrorCode;
import com.service.demo.common.exception.ApiException;
import com.service.demo.domain.user.dto.UpdateProfileRequest;
import com.service.demo.domain.user.dto.UserResponse;
import com.service.demo.domain.user.entity.UserEntity;
import com.service.demo.domain.user.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public UserResponse getById(Long id) {
        UserEntity user = userMapper.findById(id);
        if (user == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "User not found");
        }
        return new UserResponse(user.getId(), user.getLoginId(), user.getUsername(), user.getEmail(), user.getTelno());
    }

    @Transactional
    public UserResponse updateProfile(Long id, UpdateProfileRequest req) {
        UserEntity user = userMapper.findById(id);
        if (user == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "User not found");
        }
        user.setId(id);
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setTelno(req.getTelno());
        userMapper.updateProfile(user);
        UserEntity updated = userMapper.findById(id);
        return new UserResponse(updated.getId(), updated.getLoginId(), updated.getUsername(), updated.getEmail(), updated.getTelno());
    }
}
