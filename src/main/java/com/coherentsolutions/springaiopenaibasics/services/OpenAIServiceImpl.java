package com.coherentsolutions.springaiopenaibasics.services;

import com.coherentsolutions.springaiopenaibasics.exceptions.OpenAIServiceException;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

/**
 * Implementation of OpenAIService that uses Spring AI's ChatModel
 * to interact with OpenAI's API. This service converts user questions
 * into AI-generated responses.
 * 
 * WHY this service layer exists:
 * 1. Abstracts AI operations from business logic
 * 2. Provides a clean interface for different types of consumers (controllers, tasks, etc.)
 * 3. Enables easy testing through interface mocking
 * 4. Centralizes AI-related error handling and logging
 * 5. Allows for future enhancements (caching, retry logic, etc.) without changing callers
 */
@Service
public class OpenAIServiceImpl implements OpenAIService {

    private final ChatModel chatModel;

    /**
     * WHY constructor injection instead of field injection (@Autowired)?
     * 1. Makes dependencies explicit and immutable (final fields)
     * 2. Enables easier unit testing with mock injection
     * 3. Prevents NullPointerException issues in tests
     * 4. Follows Spring best practices for mandatory dependencies
     * 5. Makes the class more portable and less coupled to Spring framework
     */
    public OpenAIServiceImpl(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Override
    public String getAnswer(String question) {
        // WHY validate input parameters?
        // 1. Provides clear error messages to callers
        // 2. Prevents unnecessary API calls and costs
        // 3. Demonstrates defensive programming practices
        // 4. Teaches input validation as a fundamental practice
        if (question == null || question.trim().isEmpty()) {
            throw new IllegalArgumentException("Question cannot be null or empty");
        }

        try {
            // WHY use Spring AI's Prompt class instead of raw strings?
            // 1. Provides structure for future enhancements (options, metadata)
            // 2. Enables consistent handling of prompt-related features
            // 3. Supports future template integration and parameter injection
            // 4. Maintains type safety and IDE support
            Prompt prompt = new Prompt(question);

            // WHY call() method instead of other alternatives?
            // call() is synchronous and simple - perfect for learning
            // stream() would be for real-time responses (covered in later branches)
            ChatResponse response = chatModel.call(prompt);

            // WHY this specific chain: getResult().getOutput().getText()?
            // 1. ChatResponse can contain multiple Generation results
            // 2. getResult() gets the first (primary) generation
            // 3. getOutput() gets the AssistantMessage containing the response
            // 4. getText() extracts the actual text content
            // This chain demonstrates Spring AI's structured response handling
            return response.getResult().getOutput().getText();

        } catch (Exception e) {
            // WHY wrap exceptions in custom exception?
            // 1. Hides implementation details (Spring AI, OpenAI) from callers
            // 2. Provides consistent error handling across the application
            // 3. Enables future error categorization (network, auth, rate limit)
            // 4. Allows for centralized error handling strategies
            throw new OpenAIServiceException("Failed to get AI response", e);
        }
    }
}