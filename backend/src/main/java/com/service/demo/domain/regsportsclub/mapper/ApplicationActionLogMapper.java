package com.service.demo.domain.regsportsclub.mapper;

import com.service.demo.domain.regsportsclub.entity.ApplicationActionLogEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApplicationActionLogMapper {
    int insert(ApplicationActionLogEntity entity);
}
