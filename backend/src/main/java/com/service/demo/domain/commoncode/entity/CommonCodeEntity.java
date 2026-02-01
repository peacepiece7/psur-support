package com.service.demo.domain.commoncode.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommonCodeEntity {
    private Long id;
    private Long groupId;
    private String code;
    private String codeName;
    private String childGroupCode;
    private Integer sortOrder;
    private String description;
    private Boolean isActive;

}
