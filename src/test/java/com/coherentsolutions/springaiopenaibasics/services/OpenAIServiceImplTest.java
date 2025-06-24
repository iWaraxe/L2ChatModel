package com.coherentsolutions.springaiopenaibasics.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.*;

/**
 * Integration tests for OpenAIService with real API calls.
 * 
 * WHY integration tests?
 * 1. Validates end-to-end functionality with real AI service
 * 2. Tests actual network communication and response parsing
 * 3. Catches issues that mocks might miss (serialization, authentication, etc.)
 * 4. Builds confidence in the complete integration
 * 
 * WHY @TestPropertySource?
 * - Allows enabling/disabling expensive tests via properties
 * - Can override settings for test environment
 * - Provides flexibility for CI/CD pipeline configuration
 */
@SpringBootTest
@TestPropertySource(properties = {
    "test.integration.enabled=true",
    "spring.ai.openai.chat.options.maxTokens=50"  // Limit tokens to reduce test costs
})
class OpenAIServiceImplTest {

    @Autowired
    OpenAIService openAIService;

    @Test
    void getAnswer_WithSimpleQuestion_ShouldReturnValidResponse() {
        // Given
        String question = "What is 2+2?";
        
        // When
        String answer = openAIService.getAnswer(question);
        
        // Then
        // WHY these specific assertions?
        // 1. Ensures we get a response (not null)
        // 2. Ensures response has content (not empty)
        // 3. Reasonable length check (AI should provide substantial answer)
        assertThat(answer)
            .as("AI should provide a non-null response")
            .isNotNull()
            .as("AI should provide a non-empty response")
            .isNotBlank()
            .as("AI should provide a meaningful response (more than just a number)")
            .hasSizeGreaterThan(1);
        
        // WHY print the response?
        // Helps with manual verification during development and debugging
        System.out.println("Question: " + question);
        System.out.println("Answer: " + answer);
    }

    @Test
    void getAnswer_WithComplexQuestion_ShouldReturnDetailedResponse() {
        // Given - A question that requires some reasoning
        String question = "Explain the benefits of dependency injection in Spring.";
        
        // When
        String answer = openAIService.getAnswer(question);
        
        // Then
        assertThat(answer)
            .isNotNull()
            .isNotBlank()
            .as("Complex questions should generate substantial responses")
            .hasSizeGreaterThan(20);  // Expect more detailed answer
        
        // WHY check for key terms?
        // Validates that AI understands the context and provides relevant response
        assertThat(answer.toLowerCase())
            .as("Answer should contain relevant terminology")
            .containsAnyOf("spring", "dependency", "injection", "bean");
            
        System.out.println("Complex Question: " + question);
        System.out.println("Detailed Answer: " + answer);
    }

    @Test
    void getAnswer_WithCreativePrompt_ShouldReturnCreativeResponse() {
        // Given - Test creative capabilities
        String question = "Tell me a dad joke about programming.";
        
        // When
        String answer = openAIService.getAnswer(question);
        
        // Then
        assertThat(answer)
            .isNotNull()
            .isNotBlank();
        
        // WHY looser assertions for creative content?
        // Creative responses are more variable and harder to validate programmatically
        // Focus on basic structure rather than content specifics
        System.out.println("Creative Prompt: " + question);
        System.out.println("Creative Response: " + answer);
    }

    /**
     * Performance test to ensure reasonable response times.
     * 
     * WHY performance testing?
     * 1. AI APIs can be slow - need to understand baseline performance
     * 2. Helps identify when to implement caching or async processing
     * 3. Sets expectations for user experience
     */
    @Test
    void getAnswer_ResponseTime_ShouldBeReasonable() {
        // Given
        String question = "What is Spring Boot?";
        long startTime = System.currentTimeMillis();
        
        // When
        String answer = openAIService.getAnswer(question);
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        
        // Then
        assertThat(answer).isNotNull().isNotBlank();
        
        // WHY 30 second timeout?
        // OpenAI typically responds within 5-15 seconds for simple queries
        // 30 seconds allows for network variations while catching real issues
        assertThat(responseTime)
            .as("Response time should be reasonable for user experience")
            .isLessThan(30_000);  // 30 seconds max
        
        System.out.println("Response time: " + responseTime + "ms");
        System.out.println("Question: " + question);
        System.out.println("Answer: " + answer);
    }
}