package com.example.test.exception;

import org.springframework.http.HttpStatus;

public class NoPatientWithSuchIdException extends PatientException{
    public NoPatientWithSuchIdException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}