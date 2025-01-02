package com.ranjabi.urlshortener.exception;

public class JwtProcessingException extends RuntimeException {
    public JwtProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
