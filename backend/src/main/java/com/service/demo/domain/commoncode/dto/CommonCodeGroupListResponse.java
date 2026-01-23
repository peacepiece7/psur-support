package com.service.demo.domain.commoncode.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Paged common code group response")
public class CommonCodeGroupListResponse {
    @Schema(description = "Result items")
    private final List<CommonCodeGroupSummaryResponse> items;
    @Schema(description = "Next offset for infinite scroll", example = "20")
    private final Integer nextOffset;
    @Schema(description = "Whether more items are available", example = "true")
    private final Boolean hasNext;
    @Schema(description = "Applied limit size", example = "20")
    private final Integer limit;

    public CommonCodeGroupListResponse(List<CommonCodeGroupSummaryResponse> items,
                                       Integer nextOffset,
                                       Boolean hasNext,
                                       Integer limit) {
        this.items = items;
        this.nextOffset = nextOffset;
        this.hasNext = hasNext;
        this.limit = limit;
    }

    public List<CommonCodeGroupSummaryResponse> getItems() {
        return items;
    }

    public Integer getNextOffset() {
        return nextOffset;
    }

    public Boolean getHasNext() {
        return hasNext;
    }

    public Integer getLimit() {
        return limit;
    }
}
