package com.example.test.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PatientExceptionHandler {

    @ExceptionHandler(PatientException.class)
    protected ResponseEntity<ResponseBody> handlePatientExceptions(PatientException exception) {
        var exceptionBody = ResponseBody.builder()
                .status(exception.getHttpStatus().value())
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(exceptionBody, exception.getHttpStatus());
    }
}