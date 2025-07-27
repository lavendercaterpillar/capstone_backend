package com.ellie.capstone.exception;

/**
 * Custom exception for cases when a resource (e.g. Project) is not found.
 */
public class ResourceNotFoundException extends RuntimeException {

    // Constructor with a custom message
    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Optional: constructor with message and cause (e.g., another exception)
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
