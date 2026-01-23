package com.service.demo.domain.sportsclub.service;

import com.service.demo.common.error.ErrorCode;
import com.service.demo.common.exception.ApiException;
import com.service.demo.domain.sportsclub.dto.SportsClubCreateRequest;
import com.service.demo.domain.sportsclub.dto.SportsClubListResponse;
import com.service.demo.domain.sportsclub.dto.SportsClubResponse;
import com.service.demo.domain.sportsclub.dto.SportsClubUpdateRequest;
import com.service.demo.domain.sportsclub.entity.SportsClubCategoryEntity;
import com.service.demo.domain.sportsclub.entity.SportsClubEntity;
import com.service.demo.domain.sportsclub.mapper.SportsClubMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SportsClubService {
    private final SportsClubMapper sportsClubMapper;

    public SportsClubService(SportsClubMapper sportsClubMapper) {
        this.sportsClubMapper = sportsClubMapper;
    }

    @Transactional
    public SportsClubResponse create(SportsClubCreateRequest req) {
        SportsClubEntity entity = new SportsClubEntity();
        entity.setName(req.getName());
        entity.setLocation(req.getLocation());
        entity.setRepresentativeName(req.getRepresentativeName());
        entity.setRepresentativeTelno(req.getRepresentativeTelno());
        entity.setClubNo(req.getClubNo());
        entity.setBusinessNo(req.getBusinessNo());
        entity.setClubRoleCodeId(req.getClubRoleCodeId());

        sportsClubMapper.insertClub(entity);
        saveCategories(entity.getId(), req.getCategoryIds());

        return getById(entity.getId());
    }

    public SportsClubResponse getById(Long id) {
        SportsClubEntity entity = sportsClubMapper.findById(id);
        if (entity == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "Sports club not found");
        }
        List<Long> categoryIds = sportsClubMapper.findCategoryIdsByClubId(id);
        return toResponse(entity, categoryIds);
    }

    public List<SportsClubListResponse> list() {
        List<SportsClubEntity> entities = sportsClubMapper.findAll();
        List<SportsClubListResponse> results = new ArrayList<>();
        for (SportsClubEntity entity : entities) {
            results.add(new SportsClubListResponse(
                    entity.getId(), entity.getName(), entity.getLocation(),
                    entity.getRepresentativeName(), entity.getRepresentativeTelno(),
                    entity.getClubNo(), entity.getBusinessNo(),
                    entity.getClubRoleCodeId(), entity.getIsActive()
            ));
        }
        return results;
    }

    @Transactional
    public SportsClubResponse update(Long id, SportsClubUpdateRequest req) {
        SportsClubEntity entity = sportsClubMapper.findById(id);
        if (entity == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "Sports club not found");
        }

        entity.setId(id);
        entity.setName(req.getName());
        entity.setLocation(req.getLocation());
        entity.setRepresentativeName(req.getRepresentativeName());
        entity.setRepresentativeTelno(req.getRepresentativeTelno());
        entity.setClubNo(req.getClubNo());
        entity.setBusinessNo(req.getBusinessNo());
        entity.setClubRoleCodeId(req.getClubRoleCodeId());
        if (req.getIsActive() != null) {
            entity.setIsActive(req.getIsActive());
        }

        sportsClubMapper.updateClub(entity);
        saveCategories(id, req.getCategoryIds());

        return getById(id);
    }

    @Transactional
    public void delete(Long id) {
        SportsClubEntity entity = sportsClubMapper.findById(id);
        if (entity == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "Sports club not found");
        }
        sportsClubMapper.softDeleteClub(id);
        sportsClubMapper.deleteCategoriesByClubId(id);
    }

    private void saveCategories(Long clubId, List<Long> categoryIds) {
        sportsClubMapper.deleteCategoriesByClubId(clubId);
        if (categoryIds == null || categoryIds.isEmpty()) {
            return;
        }
        List<SportsClubCategoryEntity> items = new ArrayList<>();
        for (Long categoryId : categoryIds) {
            items.add(new SportsClubCategoryEntity(clubId, categoryId));
        }
        sportsClubMapper.insertCategories(items);
    }

    private SportsClubResponse toResponse(SportsClubEntity entity, List<Long> categoryIds) {
        return new SportsClubResponse(
                entity.getId(), entity.getName(), entity.getLocation(),
                entity.getRepresentativeName(), entity.getRepresentativeTelno(),
                entity.getClubNo(), entity.getBusinessNo(),
                entity.getClubRoleCodeId(), entity.getIsActive(), categoryIds
        );
    }
}
