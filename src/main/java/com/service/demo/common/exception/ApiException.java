package com.service.demo.common.exception;

import com.service.demo.common.error.ErrorCodeIfs;

public class ApiException extends RuntimeException {
    private final ErrorCodeIfs errorCodeIfs;
    private final String errorDescription;

    public ApiException(ErrorCodeIfs errorCodeIfs) {
        super(errorCodeIfs.getDescription());
        this.errorCodeIfs = errorCodeIfs;
        this.errorDescription = errorCodeIfs.getDescription();
    }

    public ApiException(ErrorCodeIfs errorCodeIfs, String errorDescription) {
        super(errorDescription);
        this.errorCodeIfs = errorCodeIfs;
        this.errorDescription = errorDescription;
    }

    public ErrorCodeIfs getErrorCodeIfs() {
        return errorCodeIfs;
    }

    public String getErrorDescription() {
        return errorDescription;
    }
}
