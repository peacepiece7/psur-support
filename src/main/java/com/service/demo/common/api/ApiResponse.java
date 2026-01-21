package com.service.demo.common.api;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Standard API wrapper")
public class ApiResponse<T> {
    @Schema(description = "Application-level result code", example = "200")
    private final int resultCode;
    @Schema(description = "Application-level result message", example = "OK")
    private final String resultMessage;
    @Schema(description = "Response payload")
    private final T data;

    private ApiResponse(int resultCode, String resultMessage, T data) {
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
        this.data = data;
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(200, "OK", data);
    }

    public static <T> ApiResponse<T> fail(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public T getData() {
        return data;
    }
}
