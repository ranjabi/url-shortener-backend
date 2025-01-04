package com.ranjabi.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ranjabi.urlshortener.dto.response.ErrorResponse;

import jakarta.validation.ConstraintViolationException;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError error = e.getFieldError();
        ErrorResponse<Void> body = ErrorResponse.ofMessage(error.getField() + " " + error.getDefaultMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse<Void>> handleConstraintViolationException(ConstraintViolationException e) {
        String[] parts = e.getMessage().split(":");
        String errorMessage = parts.length > 1 ? parts[1].trim() : "";
        ErrorResponse<Void> body = ErrorResponse.ofMessage(errorMessage);
        
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}