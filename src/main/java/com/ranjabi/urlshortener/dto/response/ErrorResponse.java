package com.ranjabi.urlshortener.dto.response;

import lombok.Data;

@Data
public class ErrorResponse<T> {
    private final String message;
    private final T data;

    public ErrorResponse(String message) {
        this.message = message;
        this.data = null;
    }

    public ErrorResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

}
