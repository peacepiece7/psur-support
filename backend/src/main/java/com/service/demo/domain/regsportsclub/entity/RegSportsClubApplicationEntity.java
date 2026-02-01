package com.service.demo.domain.regsportsclub.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegSportsClubApplicationEntity {
    private Long id;
    private Long applyId;
    private String name;
    private String location;
    private String representativeName;
    private String representativeTelno;
    private String businessNo;
    private Long clubRoleCodeId;
    private Long operatingSportParentCodeId;
    private Long operatingSportChildCodeId;
    private Long approvedClubId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

}
