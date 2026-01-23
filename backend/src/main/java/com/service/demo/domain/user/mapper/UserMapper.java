package com.service.demo.domain.user.mapper;

import com.service.demo.domain.user.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    int insert(UserEntity user);
    UserEntity findByLoginId(@Param("loginId") String loginId);
    UserEntity findById(@Param("id") Long id);
    int updateProfile(UserEntity user);
    int updatePassword(@Param("id") Long id, @Param("passwordHash") String passwordHash);
    int updateLastLogin(@Param("id") Long id);
}
