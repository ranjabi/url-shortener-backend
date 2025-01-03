package com.ranjabi.urlshortener.dto.response;

public interface Response<T> {
    String getMessage();
    T getData();
}