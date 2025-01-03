package com.ranjabi.urlshortener.dto.response;

import lombok.Data;

@Data
public class ErrorResponse<T> implements Response<T> {
    private final String message;
    private final T data;

    public ErrorResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public static <T> ErrorResponse<T> ofMessage(String message) {
        return new ErrorResponse<>(message, null);
    }

    public static <T> ErrorResponse<T> of(String message, T data) {
        return new ErrorResponse<>(message, data);
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public T getData() {
        return data;
    }
}
