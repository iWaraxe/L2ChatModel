package com.coherentsolutions.springaiopenaibasics.services;

/**
 * Service interface for OpenAI interactions.
 * Provides methods to communicate with OpenAI's API through Spring AI.
 * 
 * WHY create an interface instead of using the implementation directly?
 * 1. Enables easy testing with mock implementations
 * 2. Supports multiple implementations (e.g., cached version, different providers)
 * 3. Follows dependency inversion principle - depend on abstractions, not concretions
 * 4. Makes the code more flexible and maintainable
 * 5. Allows for future proxy implementations (logging, monitoring, retry logic)
 */
public interface OpenAIService {
    
    /**
     * Gets an AI-generated answer for the given question.
     * 
     * @param question The question to ask the AI model
     * @return The AI-generated response as a string
     * @throws IllegalArgumentException if question is null or empty
     * @throws OpenAIServiceException if the AI service call fails
     * 
     * WHY document exceptions in interface?
     * 1. Makes the contract explicit for implementers and callers
     * 2. Enables proper error handling in calling code
     * 3. Documents expected behavior without looking at implementation
     */
    String getAnswer(String question);
}