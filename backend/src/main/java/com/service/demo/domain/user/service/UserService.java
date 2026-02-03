package com.service.demo.domain.user.service;

import com.service.demo.common.error.ErrorCode;
import com.service.demo.common.exception.ApiException;
import com.service.demo.domain.role.entity.UserRoleEntity;
import com.service.demo.domain.role.mapper.UserRoleMapper;
import com.service.demo.domain.user.dto.MeDetailResponse;
import com.service.demo.domain.user.dto.SportsClubSummary;
import com.service.demo.domain.user.dto.UpdateProfileRequest;
import com.service.demo.domain.user.dto.UserListResponse;
import com.service.demo.domain.user.dto.UserResponse;
import com.service.demo.domain.user.dto.UserRoleSummary;
import com.service.demo.domain.user.entity.UserEntity;
import com.service.demo.domain.user.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;

    public UserService(UserMapper userMapper, UserRoleMapper userRoleMapper) {
        this.userMapper = userMapper;
        this.userRoleMapper = userRoleMapper;
    }

    public UserResponse getById(Long id) {
        UserEntity user = userMapper.findById(id);
        if (user == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "User not found");
        }
        return new UserResponse(user.getId(), user.getLoginId(), user.getUsername(), user.getEmail(), user.getTelno());
    }

    public MeDetailResponse getMeDetail(Long id) {
        UserEntity user = userMapper.findById(id);
        if (user == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "User not found");
        }
        UserResponse userResponse = new UserResponse(
            user.getId(), user.getLoginId(), user.getUsername(), user.getEmail(), user.getTelno()
        );

        List<UserRoleEntity> roles = userRoleMapper.findAllWithRole(id, false);
        List<UserRoleSummary> roleSummaries = new ArrayList<>();
        for (UserRoleEntity role : roles) {
            roleSummaries.add(new UserRoleSummary(
                role.getRoleId(),
                role.getRoleCode(),
                role.getRoleName(),
                role.getRoleType()
            ));
        }

        List<SportsClubSummary> sportsClubList = new ArrayList<>();

        return new MeDetailResponse(userResponse, roleSummaries, sportsClubList);
    }

    public List<UserListResponse> listUsers(boolean includeInactive) {
        List<UserEntity> users = userMapper.findAll(includeInactive);
        List<UserListResponse> result = new ArrayList<>();
        for (UserEntity user : users) {
            result.add(new UserListResponse(
                user.getId(),
                user.getLoginId(),
                user.getUsername(),
                user.getEmail(),
                user.getTelno(),
                user.getStatus(),
                user.getIsActive(),
                user.getLastLoginAt(),
                user.getCreatedAt()
            ));
        }
        return result;
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
