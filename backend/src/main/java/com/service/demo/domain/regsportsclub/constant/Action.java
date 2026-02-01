package com.service.demo.domain.regsportsclub.constant;

import lombok.Getter;

@Getter
public enum Action {
    SAVE("save"),
    APPLY("apply"),
    RECEIPT("receipt"),
    REVIEW("review"),
    APPROVE("approve"),
    REJECT("reject");

    private final String bpmValue;

    Action(String bpmValue) {
        this.bpmValue = bpmValue;
    }
}
