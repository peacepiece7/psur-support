package com.service.demo.domain.role.mapper;

import com.service.demo.domain.role.entity.UserRoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserRoleMapper {
    int upsertUserRole(UserRoleEntity entity);
    int softDeleteUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);
    int softDeleteByUserId(@Param("userId") Long userId);
    int softDeleteByUserIdExcludingRoleIds(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);
    UserRoleEntity findByUserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);
    List<UserRoleEntity> findAllWithRole(@Param("userId") Long userId,
                                         @Param("includeInactive") boolean includeInactive);
    List<Long> findActiveRoleIdsByUserId(@Param("userId") Long userId);
}
