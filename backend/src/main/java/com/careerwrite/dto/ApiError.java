package com.careerwrite.dto;

// A tiny, consistent shape for every error response: { "message": "..." }
// so the frontend JS can always do:  const err = await res.json(); alert(err.message);
public class ApiError {

    private String message;

    public ApiError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
