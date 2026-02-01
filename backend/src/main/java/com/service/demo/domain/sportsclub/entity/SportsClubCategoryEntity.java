package com.service.demo.domain.sportsclub.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SportsClubCategoryEntity {
    private Long clubId;
    private Long categoryId;

    public SportsClubCategoryEntity() {
    }

    public SportsClubCategoryEntity(Long clubId, Long categoryId) {
        this.clubId = clubId;
        this.categoryId = categoryId;
    }

}
