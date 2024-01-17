package com.example.test.exception;

import org.springframework.http.HttpStatus;

public class NotUniquePatientNameException extends PatientException{
    public NotUniquePatientNameException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}