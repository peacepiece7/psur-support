package com.service.demo.common.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode implements ErrorCodeIfs {
    OK(200, 200, "OK"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), 400, "Bad request"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), 401, "Unauthorized"),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), 500, "Server error"),
    CONVERT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), 500, "Entity conversion failed"),
    NULL_POINT(HttpStatus.INTERNAL_SERVER_ERROR.value(), 512, "Null point");

    private final int httpStatusCode;
    private final int errorCode;
    private final String description;

    ErrorCode(int httpStatusCode, int errorCode, String description) {
        this.httpStatusCode = httpStatusCode;
        this.errorCode = errorCode;
        this.description = description;
    }

    @Override
    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
