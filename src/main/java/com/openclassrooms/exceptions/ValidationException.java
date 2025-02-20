package com.openclassrooms.exceptions;

import org.springframework.http.HttpStatus;

public class ValidationException extends ApiException {
    public ValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}