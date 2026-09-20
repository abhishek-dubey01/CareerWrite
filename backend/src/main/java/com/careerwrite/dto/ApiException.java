package com.careerwrite.dto;

// A simple unchecked exception we throw from the service layer whenever
// something goes wrong (duplicate email, job not found, wrong password,
// duplicate application, etc). GlobalExceptionHandler turns this into a
// clean JSON error response with the given HTTP status.
import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    private final HttpStatus status;

    public ApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
