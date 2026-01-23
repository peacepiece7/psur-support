package com.service.demo.domain.sportsclub.mapper;

import com.service.demo.domain.sportsclub.entity.SportsClubCategoryEntity;
import com.service.demo.domain.sportsclub.entity.SportsClubEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SportsClubMapper {
    int insertClub(SportsClubEntity entity);
    int updateClub(SportsClubEntity entity);
    int softDeleteClub(@Param("id") Long id);
    SportsClubEntity findById(@Param("id") Long id);
    List<SportsClubEntity> findAll();

    int deleteCategoriesByClubId(@Param("clubId") Long clubId);
    int insertCategories(@Param("items") List<SportsClubCategoryEntity> items);
    List<Long> findCategoryIdsByClubId(@Param("clubId") Long clubId);
    List<SportsClubCategoryEntity> findCategoryPairsByClubIds(@Param("clubIds") List<Long> clubIds);
}
