package com.service.demo.domain.regsportsclub.mapper;

import com.service.demo.domain.regsportsclub.dto.RegSportsClubApplicationResponse;
import com.service.demo.domain.regsportsclub.entity.RegSportsClubApplyEntity;
import com.service.demo.domain.regsportsclub.entity.RegSportsClubApplicationCategoryEntity;
import com.service.demo.domain.regsportsclub.entity.RegSportsClubApplicationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RegSportsClubApplicationMapper {
    int insertApply(RegSportsClubApplyEntity entity);
    int insertApplication(RegSportsClubApplicationEntity entity);
    RegSportsClubApplyEntity findApplyById(@Param("id") Long id);
    RegSportsClubApplicationEntity findApplicationByApplyId(@Param("applyId") Long applyId);
    RegSportsClubApplicationResponse findApplicationDetail(@Param("applyId") Long applyId);
    List<RegSportsClubApplicationResponse> findAllApplications();
    int updateApplyProcessTaskId(@Param("id") Long id, @Param("processTaskId") Long processTaskId);
    int updateApprovedClubId(@Param("applyId") Long applyId, @Param("approvedClubId") Long approvedClubId);
    int deleteApplicationCategoriesByApplicationId(@Param("applicationId") Long applicationId);
    int insertApplicationCategories(@Param("applicationId") Long applicationId, @Param("categoryIds") List<Long> categoryIds);
    List<Long> findCategoryIdsByApplicationId(@Param("applicationId") Long applicationId);
    List<RegSportsClubApplicationCategoryEntity> findCategoryPairsByApplicationIds(@Param("applicationIds") List<Long> applicationIds);
}
