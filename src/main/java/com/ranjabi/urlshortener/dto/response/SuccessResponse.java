package com.ranjabi.urlshortener.dto.response;

import lombok.Data;

@Data
public class SuccessResponse<T> implements Response<T> {
    private final String message;
    private final T data;

    public SuccessResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public static <T> SuccessResponse<T> ofMessage(String message) {
        return new SuccessResponse<>(message, null);
    }

    public static <T> SuccessResponse<T> ofBody(T body) {
        return new SuccessResponse<>("", body);
    }

    public static <T> SuccessResponse<T> of(String message, T data) {
        return new SuccessResponse<>(message, data);
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
