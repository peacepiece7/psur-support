package com.service.demo.common.error;

import org.springframework.http.HttpStatus;

public enum TokenErrorCode implements ErrorCodeIfs {
    AUTHORIZATION_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED.value(), 40101, "Authorization token not found"),
    INVALID_AUTHORIZATION_TOKEN(HttpStatus.UNAUTHORIZED.value(), 40102, "Invalid authorization token");

    private final int httpStatusCode;
    private final int errorCode;
    private final String description;

    TokenErrorCode(int httpStatusCode, int errorCode, String description) {
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
