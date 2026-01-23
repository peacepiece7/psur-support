package com.service.demo.domain.sportsclub.entity;

public class SportsClubCategoryEntity {
    private Long clubId;
    private Long categoryId;

    public SportsClubCategoryEntity() {
    }

    public SportsClubCategoryEntity(Long clubId, Long categoryId) {
        this.clubId = clubId;
        this.categoryId = categoryId;
    }

    public Long getClubId() {
        return clubId;
    }

    public void setClubId(Long clubId) {
        this.clubId = clubId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
