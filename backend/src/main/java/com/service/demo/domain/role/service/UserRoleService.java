package com.service.demo.domain.role.service;

import com.service.demo.common.error.ErrorCode;
import com.service.demo.common.exception.ApiException;
import com.service.demo.domain.role.dto.UserRoleAssignRequest;
import com.service.demo.domain.role.dto.UserRoleBulkUpdateRequest;
import com.service.demo.domain.role.dto.UserRoleResponse;
import com.service.demo.domain.role.dto.UserRoleUpdateRequest;
import com.service.demo.domain.role.entity.RoleEntity;
import com.service.demo.domain.role.entity.UserRoleEntity;
import com.service.demo.domain.role.mapper.RoleMapper;
import com.service.demo.domain.role.mapper.UserRoleMapper;
import com.service.demo.domain.user.entity.UserEntity;
import com.service.demo.domain.user.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserRoleService {
    private final UserRoleMapper userRoleMapper;
    private final RoleMapper roleMapper;
    private final UserMapper userMapper;

    public UserRoleService(UserRoleMapper userRoleMapper, RoleMapper roleMapper, UserMapper userMapper) {
        this.userRoleMapper = userRoleMapper;
        this.roleMapper = roleMapper;
        this.userMapper = userMapper;
    }

    public List<UserRoleResponse> list(Long userId, boolean includeInactive) {
        if (userId != null) {
            validateUserExists(userId);
        }
        List<UserRoleEntity> rows = userRoleMapper.findAllWithRole(userId, includeInactive);
        List<UserRoleResponse> result = new ArrayList<>();
        for (UserRoleEntity row : rows) {
            result.add(toResponse(row));
        }
        return result;
    }

    public UserRoleResponse assign(UserRoleAssignRequest request) {
        validateUserExists(request.getUserId());
        validateRoleActive(request.getRoleId());
        UserRoleEntity entity = new UserRoleEntity();
        entity.setUserId(request.getUserId());
        entity.setRoleId(request.getRoleId());
        userRoleMapper.upsertUserRole(entity);
        UserRoleEntity updated = userRoleMapper.findByUserIdAndRoleId(request.getUserId(), request.getRoleId());
        if (updated == null) {
            throw new ApiException(ErrorCode.SERVER_ERROR, "Failed to assign user role");
        }
        return toResponse(updated);
    }

    @Transactional
    public UserRoleResponse update(UserRoleUpdateRequest request) {
        validateUserExists(request.getUserId());
        validateRoleExists(request.getOldRoleId());
        validateRoleActive(request.getNewRoleId());

        int deleted = userRoleMapper.softDeleteUserRole(request.getUserId(), request.getOldRoleId());
        if (deleted == 0) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "User role not found");
        }

        UserRoleEntity entity = new UserRoleEntity();
        entity.setUserId(request.getUserId());
        entity.setRoleId(request.getNewRoleId());
        userRoleMapper.upsertUserRole(entity);

        UserRoleEntity updated = userRoleMapper.findByUserIdAndRoleId(request.getUserId(), request.getNewRoleId());
        if (updated == null) {
            throw new ApiException(ErrorCode.SERVER_ERROR, "Failed to update user role");
        }
        return toResponse(updated);
    }

    public void delete(Long userId, Long roleId) {
        validateUserExists(userId);
        validateRoleExists(roleId);
        int deleted = userRoleMapper.softDeleteUserRole(userId, roleId);
        if (deleted == 0) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "User role not found");
        }
    }

    @Transactional
    public List<UserRoleResponse> bulkUpdate(UserRoleBulkUpdateRequest request) {
        validateUserExists(request.getUserId());
        List<Long> roleIds = request.getRoleIds();
        if (roleIds == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "Role IDs are required");
        }

        Set<Long> uniqueRoleIds = new HashSet<>(roleIds);
        if (!uniqueRoleIds.isEmpty()) {
            List<RoleEntity> roles = roleMapper.findByIds(new ArrayList<>(uniqueRoleIds));
            if (roles.size() != uniqueRoleIds.size()) {
                throw new ApiException(ErrorCode.BAD_REQUEST, "Role not found");
            }
            for (RoleEntity role : roles) {
                if (Boolean.FALSE.equals(role.getIsActive())) {
                    throw new ApiException(ErrorCode.BAD_REQUEST, "Role is inactive");
                }
            }
        }

        if (uniqueRoleIds.isEmpty()) {
            userRoleMapper.softDeleteByUserId(request.getUserId());
        } else {
            userRoleMapper.softDeleteByUserIdExcludingRoleIds(
                request.getUserId(),
                new ArrayList<>(uniqueRoleIds)
            );
            for (Long roleId : uniqueRoleIds) {
                UserRoleEntity entity = new UserRoleEntity();
                entity.setUserId(request.getUserId());
                entity.setRoleId(roleId);
                userRoleMapper.upsertUserRole(entity);
            }
        }

        List<UserRoleEntity> updated = userRoleMapper.findAllWithRole(request.getUserId(), false);
        List<UserRoleResponse> result = new ArrayList<>();
        for (UserRoleEntity row : updated) {
            result.add(toResponse(row));
        }
        return result;
    }

    private void validateUserExists(Long userId) {
        UserEntity user = userMapper.findById(userId);
        if (user == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "User not found");
        }
    }

    private void validateRoleExists(Long roleId) {
        RoleEntity role = roleMapper.findById(roleId);
        if (role == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "Role not found");
        }
    }

    private void validateRoleActive(Long roleId) {
        RoleEntity role = roleMapper.findById(roleId);
        if (role == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "Role not found");
        }
        if (Boolean.FALSE.equals(role.getIsActive())) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "Role is inactive");
        }
    }

    private UserRoleResponse toResponse(UserRoleEntity row) {
        return new UserRoleResponse(
            row.getId(),
            row.getUserId(),
            row.getRoleId(),
            row.getRoleCode(),
            row.getRoleName(),
            row.getRoleType(),
            row.getIsActive(),
            row.getAssignedAt(),
            row.getUpdatedAt(),
            row.getDeletedAt()
        );
    }
}
