package com.service.demo.domain.commoncode.mapper;

import com.service.demo.domain.commoncode.entity.CommonCodeEntity;
import com.service.demo.domain.commoncode.entity.CommonCodeGroupEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommonCodeMapper {
    CommonCodeGroupEntity findGroupByCode(@Param("groupCode") String groupCode);
    List<CommonCodeGroupEntity> findAllGroups();
    List<CommonCodeGroupEntity> findGroupsByParentId(@Param("parentId") Long parentId);
    List<CommonCodeGroupEntity> findGroupsByParentIds(@Param("parentIds") List<Long> parentIds);
    List<CommonCodeEntity> findCodesByGroupIds(@Param("groupIds") List<Long> groupIds);
}
