package com.service.demo.domain.commoncode.service;

import com.service.demo.domain.commoncode.mapper.CommonCodeMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class CommonCodeLookupService {
    private final CommonCodeMapper commonCodeMapper;
    private final ConcurrentMap<String, Optional<Long>> codeIdCache = new ConcurrentHashMap<>();

    public CommonCodeLookupService(CommonCodeMapper commonCodeMapper) {
        this.commonCodeMapper = commonCodeMapper;
    }

    public Long getCodeId(String groupCode, String code) {
        String key = groupCode + ":" + code;
        Optional<Long> cached = codeIdCache.computeIfAbsent(
                key,
                k -> Optional.ofNullable(commonCodeMapper.findCodeIdByGroupCodeAndCode(groupCode, code))
        );
        return cached.orElse(null);
    }

    public void evictCodeId(String groupCode, String code) {
        codeIdCache.remove(groupCode + ":" + code);
    }
}
