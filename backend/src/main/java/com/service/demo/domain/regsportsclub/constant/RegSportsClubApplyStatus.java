package com.service.demo.domain.regsportsclub.constant;

import lombok.Getter;

@Getter
public enum RegSportsClubApplyStatus {
    SAVED("SAVED"),
    APPLY("APPLY"),
    RECEIVED("RECEIVED"),
    RECEIVED_REJECTED("RECEIVED_REJECTED"),
    REVIEW("REVIEW"),
    REVIEW_REJECTED("REVIEW_REJECTED"),
    APPROVED("APPROVED"),
    APPROVED_REJECTED("APPROVED_REJECTED");

    public static final String GROUP_CODE = "REG_SPORTS_CLUB_APPLY_STATUS";

    private final String code;

    RegSportsClubApplyStatus(String code) {
        this.code = code;
    }

}
