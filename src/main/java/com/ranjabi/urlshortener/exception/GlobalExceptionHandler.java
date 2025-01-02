package com.ranjabi.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ranjabi.urlshortener.dto.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JwtProcessingException.class)
    public ResponseEntity<ErrorResponse<Void>> handleJwtProcessingException(JwtProcessingException e) {
        ErrorResponse<Void> body = ErrorResponse.ofMessage(e.getMessage());

        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<ErrorResponse<Void>> handleDuplicateUsernameException(DuplicateUsernameException e) {
        ErrorResponse<Void> body = ErrorResponse.ofMessage("Username already exists");

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }
}