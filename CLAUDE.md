# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a comprehensive Spring AI demonstration project for Lecture 2 of the Spring AI Mastery course, focusing on OpenAI API integration fundamentals and progressive AI provider capabilities. The project evolves through 17 git branches, each introducing new Spring AI concepts incrementally.

## Build and Test Commands

```bash
# Build the project
./mvnw clean package

# Run all tests
./mvnw test

# Run a specific test class
./mvnw test -Dtest=OpenAIServiceImplTest

# Run the application
./mvnw spring-boot:run

# Run with specific profile  
./mvnw spring-boot:run -Dspring.profiles.active=dev

# Skip tests during build
./mvnw clean package -DskipTests

# Manual testing (requires running application)
curl http://localhost:8080/api/question -X POST \
  -H "Content-Type: application/json" \
  -d '{"question": "What is Spring AI?"}'
```

## Architecture

### Current Architecture (Branch 2)
```
HTTP Request → OpenAIService → Spring AI ChatModel → OpenAI API
```

### Target Architecture (Advanced Branches)
```
HTTP Request → Controllers → Services → Spring AI Providers → Multiple AI APIs
                    ↓           ↓
                Templates → JSON Converters → Structured Responses
```

### Key Components

- **OpenAIService**: Core abstraction for AI interactions using Spring AI's ChatModel
- **SpringAiOpenAiBasicsApplication**: Main Spring Boot application class
- **ChatModel**: Spring AI abstraction for chat-based AI models (auto-configured)
- **Prompt**: Spring AI wrapper for structuring prompts and options
- **ChatResponse**: Structured response containing AI output and metadata

## Branch Progression Strategy

The project teaches Spring AI concepts through 17 incremental branches:

### Part 1: Foundation & High-Level APIs (Branches 1-12)

1. **1-openai-service** - Basic OpenAI integration with service layer
2. **2-explore-llm-capabilities** - Understanding model capabilities and limitations (current)
3. **3-create-spring-mvc-controller** - REST API endpoints for AI interactions
4. **4-postman-demo** - API testing and documentation with Postman
5. **5-using-prompt-templates** - Structured prompting with Spring Templates
6. **6-custom-response-format** - Custom response models and formatting
7. **7-response-in-json-format** - Structured JSON output from AI models
8. **8-response-in-json-format-update** - Enhanced JSON response handling
9. **9-binding-with-json-schema** - Type-safe JSON schema validation
10. **10-binding-with-info** - Advanced data binding with metadata
11. **11-anthropic-claude** - High-level Anthropic integration
12. **12-local-ollama** - Self-hosted models with Ollama

### Part 2: Low-Level APIs & Advanced Integrations (Branches 13-17)

13. **13-low-level-OpenAiApi** - Direct OpenAI API control for cost tracking and metadata
14. **14-low-level-AnthropicApi** - Constitutional AI with system prompts and safety
15. **15-low-level-OllamaApi** - Local deployment with model management
16. **16-perplexity-search-ai** - Real-time web search integration
17. **17-mistral-ai** - European AI with multilingual support

Each branch adds 1-3 key concepts with 50-200 lines of code change.

## Configuration

### Required Environment Variables
- `OPENAI_API_KEY` - Required for OpenAI API access
- `ANTHROPIC_API_KEY` - Required for Anthropic integration (branch 11+)
- `MISTRAL_AI_API_KEY` - Required for Mistral AI integration (branch 17)
- `PERPLEXITY_API_KEY` - Required for Perplexity search (branch 16)

### Application Properties (Current Branch)
```properties
spring.application.name=Spring AI OpenAI Basics
server.port=8080

# OpenAI API configuration
spring.ai.openai.api-key=${OPENAI_API_KEY}

# Model settings
spring.ai.openai.chat.options.model=gpt-3.5-turbo
spring.ai.openai.chat.options.temperature=1.0
spring.ai.openai.chat.options.maxTokens=150
```

### Progressive Configuration Evolution
```yaml
# Branch 1-2: Basic OpenAI
spring.ai.openai.api-key=${OPENAI_API_KEY}

# Branch 5+: Template-based prompts
spring.ai.openai.chat.options.model=gpt-3.5-turbo
spring.ai.openai.chat.options.temperature=0.7

# Branch 11+: Multi-provider support
spring.ai.anthropic.api-key=${ANTHROPIC_API_KEY}
spring.ai.ollama.base-url=http://localhost:11434

# Branch 13+: Low-level API control
spring.ai.openai.api.url=https://api.openai.com
spring.ai.openai.api.timeout=60s
```

## Development Guidelines

### Spring AI Version Compatibility
- Using Spring AI 1.0.0 (released version)
- Spring Boot 3.4.4 for optimal compatibility
- Java 21 as the target runtime
- Maven-based build system

### Code Style Preferences
- Constructor injection over field injection
- Service layer abstraction for AI operations
- Proper error handling with try-catch blocks
- Use of Spring AI's Prompt and ChatResponse abstractions
- Immutable DTOs with record classes in advanced branches

### Testing Approach
- Integration tests with @SpringBootTest for AI service testing
- Mock external dependencies in unit tests
- Test both success and error scenarios
- Cost-conscious testing - use mocks for extensive test suites
- Real API tests for final validation (requires API keys)

### Modern Testing Pattern (Advanced Branches)
```java
@SpringBootTest
class OpenAIServiceImplTest {
    @Autowired
    OpenAIService openAIService;
    
    @Test
    void getAnswer_ShouldReturnValidResponse() {
        String answer = openAIService.getAnswer("Tell me a dad joke.");
        assertThat(answer).isNotBlank();
    }
}
```

## API Endpoints

### Current Endpoints (Branch 2)
- Service-only implementation - no REST endpoints yet

### Future Endpoints (Branch 3+)
- `POST /api/question` - Send question to AI service
  - Request: `{"question": "Your question here"}`
  - Response: `{"answer": "AI response"}`
- `GET /api/health` - Health check endpoint

### Advanced Endpoints (Branch 5+)
- `POST /api/capital` - Get capital city with structured response
- `POST /api/templates` - Template-based prompt processing
- `POST /api/json-schema` - Type-safe JSON response handling

## Testing and Demonstration

### Command Line Testing
```bash
# Health check (branch 3+)
curl http://localhost:8080/api/health

# Basic question endpoint (branch 3+)
curl -X POST http://localhost:8080/api/question \
  -H "Content-Type: application/json" \
  -d '{"question": "What is Spring AI?"}'

# Template-based request (branch 5+)
curl -X POST http://localhost:8080/api/capital \
  -H "Content-Type: application/json" \
  -d '{"stateOrCountry": "France"}'
```

### Postman Integration (Branch 4+)
For trainers and visual demonstrations:

1. **Import Collection**: Available in each branch's postman/ directory
2. **Import Environment**: Local Development environment variables
3. **Select Environment**: Set appropriate API keys
4. **Run Collection**: Test all endpoints visually

**Collection Evolution by Branch**:
- Branch 4: Basic API testing
- Branch 5: Template-based requests
- Branch 7: JSON schema validation
- Branch 9: Complex data binding scenarios

## Common Tasks

### Adding New AI Provider Integration
1. Add provider dependency to pom.xml
2. Configure provider settings in application.properties
3. Create provider-specific service implementation
4. Add configuration class for provider-specific options
5. Update controllers to use new provider
6. Add integration tests for new provider

### Implementing Prompt Templates
1. Create .st template files in src/main/resources/templates/
2. Use PromptTemplate class to load and render templates
3. Pass parameters via Map to template.render()
4. Handle template loading errors gracefully

### Adding JSON Schema Support
1. Define response model classes with Jackson annotations
2. Use BeanOutputConverter for type-safe conversion
3. Add JSON schema generation for AI guidance
4. Implement proper error handling for malformed JSON

## Documentation Philosophy

This is an **educational project** with specific documentation requirements:

### Core Principles
- **WHY over HOW**: Always explain the reasoning behind architectural decisions
- **Trade-offs**: Document PROS and CONS of different approaches  
- **Alternatives**: Discuss what other options were considered and why they were rejected
- **Learning Objectives**: Clear goals for each implementation phase
- **Pitfalls**: Common mistakes and how to avoid them

### Documentation Requirements

**When implementing any new feature or branch:**

1. **Create branch documentation** in `docs/branches/XX-branch-name.md`:
   - Learning objectives for the branch
   - Architectural decisions made and WHY
   - Code patterns introduced with explanations
   - PROS/CONS of the chosen approach
   - Alternative solutions considered and rejected
   - Preparation for future enhancements

2. **Create ADRs** in `docs/decisions/` for significant architectural decisions:
   - Follow the standard ADR format
   - Include context, options considered, and rationale
   - Document consequences and trade-offs

3. **Update documentation index** in `docs/README.md`

### Educational Focus Areas
- **Architecture Evolution**: How each branch builds on previous concepts
- **Pattern Teaching**: Reusable design patterns and when to use them
- **Decision Rationale**: Why specific technologies/approaches were chosen
- **Common Pitfalls**: What mistakes learners typically make and how to avoid them
- **Future Evolution**: How current decisions prepare for upcoming features

### Code Documentation Standards
- Comments should explain WHY, not WHAT
- Include decision rationale in code when non-obvious
- Demonstrate patterns that students can reuse
- Show evolution path in comments when preparing for future features

## Course Context

This project is part of the Spring AI Mastery teaching progression:
- **Lectures 1**: Introduction to Spring AI (prerequisites)
- **Lecture 2**: OpenAI API Basics in Spring Boot (this project)
- **Lecture 3**: Prompt Engineering and Output Parsing
- **Lecture 4**: Building Chatbots with Spring Boot
- **Lecture 5**: Retrieval-Augmented Generation (RAG)
- **Lectures 6**: Advanced OpenAI Features and Best Practices
- **Lecture 7**: Voice Integration and Real-Time AI Communication
- **Lecture 8**: Deployment, Security, and Production
- **Lecture 9**: Model Context Protocol (MCP) - Building MCP Clients
- **Lecture 10**: Model Context Protocol (MCP) - Building MCP Servers

The goal is to demonstrate how Spring applications can integrate AI capabilities progressively, from basic API calls to sophisticated multi-provider systems.

## Key Learning Patterns

### Abstraction Evolution
```java
// Branch 1: Direct API usage
String response = openAiClient.complete(prompt);

// Branch 2: Service layer abstraction  
String response = openAIService.getAnswer(question);

// Branch 3: Controller layer with DTOs
Answer response = questionController.askQuestion(question);

// Branch 5: Template-based prompts
Answer response = templateService.processTemplate(template, params);

// Branch 7: Type-safe JSON responses
CapitalResponse response = capitalService.getCapital(request);
```

### Configuration Evolution
```properties
# Progressive configuration complexity
# Branch 1: Minimal
spring.ai.openai.api-key=${OPENAI_API_KEY}

# Branch 2: Model options
spring.ai.openai.chat.options.model=gpt-3.5-turbo
spring.ai.openai.chat.options.temperature=1.0

# Branch 5: Template support
spring.ai.openai.chat.options.maxTokens=150

# Branch 11: Multi-provider
spring.ai.anthropic.api-key=${ANTHROPIC_API_KEY}
```

### Error Handling Evolution
```java
// Branch 1: Basic error handling
try {
    return chatModel.call(prompt);
} catch (Exception e) {
    throw new RuntimeException(e);
}

// Branch 3: HTTP-aware error handling
try {
    return chatModel.call(prompt);
} catch (Exception e) {
    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
        "AI service unavailable", e);
}

// Branch 13: Provider-specific error handling with retry logic
@Retryable(value = {Exception.class}, maxAttempts = 3)
public Answer getAnswer(String question) {
    // Implementation with circuit breaker and fallback
}
```

## Postman Collection Management

- **Branch-Specific Collections**: Each branch has its own Postman collection in `postman/` directory
- **Environment Variables**: Shared Local Development environment for API keys
- **Testing Strategy**: Visual testing for trainers, automated testing for CI/CD
- **Evolution Tracking**: Collections grow in complexity to match branch capabilities

## Alternative Approaches and Trade-offs

### Why Spring AI over Direct HTTP Clients?
**Pros of Spring AI**:
- Provider abstraction enables switching between OpenAI, Anthropic, etc.
- Built-in retry and circuit breaker patterns
- Type-safe response handling with BeanOutputConverter
- Integration with Spring ecosystem (configuration, testing, etc.)

**Cons of Spring AI**:
- Additional abstraction layer may hide provider-specific features
- Learning curve for Spring AI concepts vs. direct API usage
- Potential performance overhead from abstraction

### Why Service Layer Architecture?
**Pros**:
- Clear separation of concerns between web and AI logic
- Testability - can mock service layer in controller tests
- Reusability - service can be used by multiple controllers
- Future-proofing - can swap implementations without affecting controllers

**Cons**:
- Additional complexity for simple use cases
- More boilerplate code
- Potential over-engineering for basic scenarios

### Why Progressive Branch Approach?
**Pros**:
- Learners can understand each concept incrementally
- Clear progression from simple to complex
- Each branch can be studied independently
- Easy to identify where specific concepts are introduced

**Cons**:
- More complex git history
- Potential merge conflicts when updating all branches
- Requires discipline to maintain consistency across branches

The educational value of the progressive approach outweighs the maintenance complexity for a teaching project.