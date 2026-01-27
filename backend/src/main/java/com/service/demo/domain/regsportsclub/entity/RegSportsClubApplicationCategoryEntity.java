package com.service.demo.domain.regsportsclub.entity;

public class RegSportsClubApplicationCategoryEntity {
    private Long applicationId;
    private Long categoryId;

    public RegSportsClubApplicationCategoryEntity() {
    }

    public RegSportsClubApplicationCategoryEntity(Long applicationId, Long categoryId) {
        this.applicationId = applicationId;
        this.categoryId = categoryId;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
