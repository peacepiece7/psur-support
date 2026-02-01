package com.service.demo.domain.regsportsclub.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegSportsClubApplyEntity {
    private Long id;
    private String processTaskId;
    private Long statusCodeId;
    private LocalDateTime appliedAt;
    private String applicantName;
    private String applicantTelno;
    private String applicantEmail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

}
