package com.service.demo.domain.role.mapper;

import com.service.demo.domain.role.entity.RoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {
    List<RoleEntity> findAll(@Param("includeInactive") boolean includeInactive);
    RoleEntity findById(@Param("id") Long id);
    List<RoleEntity> findByIds(@Param("ids") List<Long> ids);
}
