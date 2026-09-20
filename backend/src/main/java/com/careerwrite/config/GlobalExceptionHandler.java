package com.careerwrite.config;

import com.careerwrite.dto.ApiError;
import com.careerwrite.dto.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Catches exceptions thrown from any controller in one place, so we don't
// need try/catch blocks scattered everywhere. This is basic error handling
// only (not a huge framework) - it just turns exceptions into clean JSON.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Thrown manually by our services (e.g. "Email already registered")
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> handleApiException(ApiException ex) {
        return ResponseEntity.status(ex.getStatus()).body(new ApiError(ex.getMessage()));
    }

    // Thrown automatically when @Valid fails on a request body (e.g. missing field)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        FieldError firstError = ex.getBindingResult().getFieldErrors().stream().findFirst().orElse(null);
        String message = firstError != null ? firstError.getDefaultMessage() : "Invalid request data";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(message));
    }

    // Catch-all fallback so the frontend never gets an unreadable 500 HTML page
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError("Something went wrong: " + ex.getMessage()));
    }
}
