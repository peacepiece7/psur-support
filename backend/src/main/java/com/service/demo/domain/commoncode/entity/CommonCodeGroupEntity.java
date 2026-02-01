package com.service.demo.domain.commoncode.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommonCodeGroupEntity {
    private Long id;
    private String groupCode;
    private String groupName;
    private Long parentGroupId;
    private Integer level;
    private Integer sortOrder;
    private String description;

}
