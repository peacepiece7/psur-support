package com.service.demo.domain.role.service;

import com.service.demo.domain.role.dto.RoleResponse;
import com.service.demo.domain.role.entity.RoleEntity;
import com.service.demo.domain.role.mapper.RoleMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService {
    private final RoleMapper roleMapper;

    public RoleService(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    public List<RoleResponse> list(boolean includeInactive) {
        List<RoleEntity> roles = roleMapper.findAll(includeInactive);
        List<RoleResponse> result = new ArrayList<>();
        for (RoleEntity role : roles) {
            result.add(new RoleResponse(
                role.getId(),
                role.getRoleCode(),
                role.getRoleName(),
                role.getRoleType(),
                role.getDescription(),
                role.getIsActive()
            ));
        }
        return result;
    }
}
