package com.service.demo.common.exception;

import com.service.demo.common.api.ApiResponse;
import com.service.demo.common.error.ErrorCode;
import com.service.demo.common.error.ErrorCodeIfs;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Void>> handleApiException(ApiException e) {
        ErrorCodeIfs code = e.getErrorCodeIfs();
        return ResponseEntity
                .status(code.getHttpStatusCode())
                .body(ApiResponse.fail(code.getErrorCode(), e.getErrorDescription()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        return ResponseEntity
                .status(ErrorCode.SERVER_ERROR.getHttpStatusCode())
                .body(ApiResponse.fail(ErrorCode.SERVER_ERROR.getErrorCode(), ErrorCode.SERVER_ERROR.getDescription()));
    }
}
