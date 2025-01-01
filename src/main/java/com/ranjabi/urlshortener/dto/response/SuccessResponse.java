package com.ranjabi.urlshortener.dto.response;

import lombok.Data;

@Data
public class SuccessResponse<T> {
    private final String message;
    private final T data;

    public SuccessResponse(String message) {
        this.message = message;
        this.data = null;
    }

    public SuccessResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }
}
