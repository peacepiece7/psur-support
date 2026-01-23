package com.service.demo.common.error;

public interface ErrorCodeIfs {
    int getHttpStatusCode();
    int getErrorCode();
    String getDescription();
}
