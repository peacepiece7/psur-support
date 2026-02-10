package com.service.demo.domain.regsportsclub.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApplicationActionLogEntity {
    private Long id;
    private Long applyId;
    private String processCode;
    private String actionKey;
    private String taskKey;
    private String payloadJson;
    private Long actorId;
    private String actorRole;
    private LocalDateTime executedAt;
    private LocalDateTime createdAt;
}
