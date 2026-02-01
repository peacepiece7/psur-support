package com.service.demo.domain.sportsclub.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SportsClubEntity {
    private Long id;
    private String name;
    private String location;
    private String representativeName;
    private String representativeTelno;
    private String clubNo;
    private String businessNo;
    private Long clubRoleCodeId;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

}
