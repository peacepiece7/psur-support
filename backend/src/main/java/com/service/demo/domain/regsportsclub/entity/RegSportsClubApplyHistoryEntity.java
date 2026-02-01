package com.service.demo.domain.regsportsclub.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegSportsClubApplyHistoryEntity {
    private Long id;
    private Long applyId;
    private Long statusCodeId;
    private String handlerName;
    private String handlerTelno;
    private String handlerEmail;
    private String memo;
    private LocalDateTime processedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

}
