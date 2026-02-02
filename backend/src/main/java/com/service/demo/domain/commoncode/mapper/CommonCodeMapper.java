package com.service.demo.domain.commoncode.mapper;

import com.service.demo.domain.commoncode.entity.CommonCodeEntity;
import com.service.demo.domain.commoncode.entity.CommonCodeGroupEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommonCodeMapper {
    CommonCodeGroupEntity findGroupByCode(@Param("groupCode") String groupCode,
                                          @Param("includeInactive") boolean includeInactive);
    List<CommonCodeGroupEntity> findRootGroups();
    List<CommonCodeGroupEntity> findAllGroups();
    List<CommonCodeGroupEntity> findGroupsFiltered(@Param("name") String name,
                                                   @Param("includeInactive") boolean includeInactive,
                                                   @Param("sortColumn") String sortColumn,
                                                   @Param("sortDirection") String sortDirection,
                                                   @Param("offset") int offset,
                                                   @Param("limit") int limit);
    List<CommonCodeGroupEntity> findGroupsByParentId(@Param("parentId") Long parentId);
    List<CommonCodeGroupEntity> findGroupsByParentIds(@Param("parentIds") List<Long> parentIds,
                                                      @Param("includeInactive") boolean includeInactive);
    List<CommonCodeEntity> findCodesByGroupIds(@Param("groupIds") List<Long> groupIds,
                                               @Param("includeInactive") boolean includeInactive);
    CommonCodeEntity findCodeById(@Param("id") Long id);
    CommonCodeEntity findCodeByGroupAndCode(@Param("groupId") Long groupId, @Param("code") String code);
    Long findCodeIdByGroupCodeAndCode(@Param("groupCode") String groupCode, @Param("code") String code);
    int insertCode(CommonCodeEntity code);
    int updateCode(CommonCodeEntity code);
    int softDeleteCode(@Param("groupCode") String groupCode, @Param("code") String code);
}
