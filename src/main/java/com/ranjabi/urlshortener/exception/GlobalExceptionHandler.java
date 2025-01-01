package com.ranjabi.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ranjabi.urlshortener.dto.response.ErrorResponse;

import io.jsonwebtoken.MalformedJwtException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ErrorResponse<Void>> handleMalformedJwtException(MalformedJwtException e) {
        ErrorResponse<Void> body = new ErrorResponse<Void>("Malformed JWT token");

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<ErrorResponse<Void>> handleDuplicateUsernameException(DuplicateUsernameException e) {
        ErrorResponse<Void> body = new ErrorResponse<Void>("Username already exists");

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse<Void>> handleBadCredentialsException(BadCredentialsException e) {
        ErrorResponse<Void> body = new ErrorResponse<Void>("Username/password is wrong");

        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }
}