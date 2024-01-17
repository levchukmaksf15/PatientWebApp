package com.example.test.exception;

import org.springframework.http.HttpStatus;

public abstract class PatientException extends RuntimeException{
    public PatientException(String message) {
        super(message);
    }

    public abstract HttpStatus getHttpStatus();
}
