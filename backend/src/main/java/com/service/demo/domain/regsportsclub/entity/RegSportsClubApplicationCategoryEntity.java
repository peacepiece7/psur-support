package com.service.demo.domain.regsportsclub.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegSportsClubApplicationCategoryEntity {
    private Long applicationId;
    private Long categoryId;

    public RegSportsClubApplicationCategoryEntity() {
    }

    public RegSportsClubApplicationCategoryEntity(Long applicationId, Long categoryId) {
        this.applicationId = applicationId;
        this.categoryId = categoryId;
    }

}
