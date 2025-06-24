package com.coherentsolutions.springaiopenaibasics.services;

import com.coherentsolutions.springaiopenaibasics.exceptions.OpenAIServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.prompt.Prompt;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Mock-based unit tests for OpenAIService.
 * 
 * WHY mock tests?
 * 1. Fast execution - no network calls
 * 2. Cost-effective - no API charges
 * 3. Reliable - not dependent on external service availability
 * 4. Comprehensive - can test error scenarios easily
 * 5. Isolated testing - focuses on service logic, not API integration
 */
@ExtendWith(MockitoExtension.class)
class OpenAIServiceMockTest {

    @Mock
    private ChatModel chatModel;

    private OpenAIService openAIService;

    @BeforeEach
    void setUp() {
        // WHY constructor injection in tests?
        // Makes dependencies explicit and enables easy mock injection
        openAIService = new OpenAIServiceImpl(chatModel);
    }

    @Test
    void getAnswer_WithValidQuestion_ShouldReturnResponse() {
        // Given
        String question = "What is Spring AI?";
        String expectedAnswer = "Spring AI is a framework for integrating AI into Spring applications.";
        
        // WHY create full ChatResponse structure?
        // Tests that our service correctly extracts text from the response hierarchy
        ChatResponse mockResponse = createMockChatResponse(expectedAnswer);
        when(chatModel.call(any(Prompt.class))).thenReturn(mockResponse);

        // When
        String actualAnswer = openAIService.getAnswer(question);

        // Then
        assertThat(actualAnswer)
            .isNotNull()
            .isEqualTo(expectedAnswer);
        
        // WHY verify the interaction?
        // Ensures service actually calls the ChatModel with a Prompt
        verify(chatModel).call(any(Prompt.class));
    }

    @Test
    void getAnswer_WithNullQuestion_ShouldThrowException() {
        // When & Then
        assertThatThrownBy(() -> openAIService.getAnswer(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Question cannot be null or empty");
        
        // WHY verify no interaction?
        // Ensures validation happens before API call (performance + cost)
        verifyNoInteractions(chatModel);
    }

    @Test
    void getAnswer_WithEmptyQuestion_ShouldThrowException() {
        // When & Then
        assertThatThrownBy(() -> openAIService.getAnswer(""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Question cannot be null or empty");
        
        verifyNoInteractions(chatModel);
    }

    @Test
    void getAnswer_WithWhitespaceOnlyQuestion_ShouldThrowException() {
        // When & Then
        assertThatThrownBy(() -> openAIService.getAnswer("   "))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Question cannot be null or empty");
        
        verifyNoInteractions(chatModel);
    }

    @Test
    void getAnswer_WhenChatModelThrowsException_ShouldWrapInServiceException() {
        // Given
        String question = "Test question";
        RuntimeException originalException = new RuntimeException("Network error");
        when(chatModel.call(any(Prompt.class))).thenThrow(originalException);

        // When & Then
        assertThatThrownBy(() -> openAIService.getAnswer(question))
            .isInstanceOf(OpenAIServiceException.class)
            .hasMessageContaining("Failed to get AI response")
            .hasCause(originalException);
    }

    @Test
    void getAnswer_WithLongQuestion_ShouldHandleNormally() {
        // Given - Test with a longer question to ensure no length restrictions
        String longQuestion = "This is a very long question that tests whether our service can handle " +
                "lengthy inputs without issues. It might represent a complex user query that requires " +
                "detailed processing by the AI model.";
        String expectedAnswer = "Detailed response to long question.";
        
        ChatResponse mockResponse = createMockChatResponse(expectedAnswer);
        when(chatModel.call(any(Prompt.class))).thenReturn(mockResponse);

        // When
        String actualAnswer = openAIService.getAnswer(longQuestion);

        // Then
        assertThat(actualAnswer).isEqualTo(expectedAnswer);
        verify(chatModel).call(any(Prompt.class));
    }

    @Test
    void getAnswer_WithSpecialCharacters_ShouldHandleCorrectly() {
        // Given - Test with special characters that might cause encoding issues
        String questionWithSpecialChars = "What about émojis 🤖 and special chars: @#$%^&*()";
        String expectedAnswer = "Response handling special characters correctly.";
        
        ChatResponse mockResponse = createMockChatResponse(expectedAnswer);
        when(chatModel.call(any(Prompt.class))).thenReturn(mockResponse);

        // When
        String actualAnswer = openAIService.getAnswer(questionWithSpecialChars);

        // Then
        assertThat(actualAnswer).isEqualTo(expectedAnswer);
    }

    /**
     * Helper method to create a mock ChatResponse.
     * 
     * WHY separate helper method?
     * 1. Reduces test code duplication
     * 2. Centralizes ChatResponse structure knowledge
     * 3. Makes tests more readable and maintainable
     * 4. Demonstrates the complexity of Spring AI response structure
     */
    private ChatResponse createMockChatResponse(String responseText) {
        AssistantMessage assistantMessage = new AssistantMessage(responseText);
        Generation generation = new Generation(assistantMessage);
        return new ChatResponse(List.of(generation));
    }
}