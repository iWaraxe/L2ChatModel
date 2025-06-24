package com.coherentsolutions.springaiopenaibasics.exceptions;

/**
 * Custom exception for OpenAI service operations.
 * 
 * WHY custom exception?
 * 1. Encapsulates AI-specific errors from implementation details
 * 2. Allows consistent error handling across the application
 * 3. Enables future error categorization (rate limit, auth, network, etc.)
 * 4. Prevents leaking Spring AI or OpenAI exceptions to business logic
 */
public class OpenAIServiceException extends RuntimeException {
    
    /**
     * WHY RuntimeException instead of checked exception?
     * 1. AI failures are often not recoverable by calling code
     * 2. Reduces boilerplate try-catch blocks throughout the application
     * 3. Follows Spring's exception handling patterns
     * 4. Can be handled globally by @ControllerAdvice in future branches
     */
    public OpenAIServiceException(String message) {
        super(message);
    }
    
    public OpenAIServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}