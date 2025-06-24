# ADR-001: Choosing Spring AI Framework for AI Integration

## Status
Accepted

## Context

The Spring AI Mastery course needs to demonstrate AI integration in Spring Boot applications. For Lecture 2 (OpenAI API Basics), we must choose an approach for integrating OpenAI's API that balances educational value, enterprise readiness, and future extensibility.

## Decision

We will use the **Spring AI framework** as the primary abstraction for AI provider integration throughout the course.

## Options Considered

### Option 1: Direct HTTP Client Integration
**Approach**: Use `RestTemplate` or `WebClient` to make direct HTTP calls to OpenAI API

**Pros**:
- Complete control over HTTP requests and responses
- No additional dependencies beyond Spring Web
- Full access to OpenAI API features
- Students learn HTTP fundamentals

**Cons**:
- Significant boilerplate code for serialization/deserialization
- Manual error handling and retry logic
- No abstraction for switching AI providers
- Vendor lock-in to OpenAI API structure
- Repetitive code across different AI operations
- Security concerns with manual API key handling

### Option 2: OpenAI Java SDK
**Approach**: Use the official OpenAI Java SDK

**Pros**:
- Official library with full API support
- Maintained by OpenAI team
- Type-safe API access
- Built-in error handling

**Cons**:
- Vendor lock-in (cannot switch to Anthropic, Mistral, etc.)
- Manual integration with Spring ecosystem
- No unified approach for different AI providers
- Students learn OpenAI-specific patterns, not general AI integration
- Requires separate learning for each provider

### Option 3: Spring AI Framework (CHOSEN)
**Approach**: Use Spring AI's abstraction layer

**Pros**:
- **Provider Abstraction**: Can switch between OpenAI, Anthropic, Ollama without code changes
- **Spring Ecosystem Integration**: Natural fit with Spring Boot, configuration, testing
- **Educational Value**: Teaches abstraction principles and design patterns
- **Enterprise Features**: Built-in retry, circuit breaker, caching support
- **Type Safety**: Structured response handling with BeanOutputConverter
- **Security**: Secure API key management through Spring properties
- **Future-Proof**: New providers can be added without breaking existing code
- **Consistency**: Unified patterns across different AI capabilities
- **Testing Support**: Mock implementations and test utilities

**Cons**:
- Additional abstraction layer may hide some provider-specific features
- Learning curve for Spring AI concepts
- Dependency on relatively new framework
- Potential performance overhead from abstraction

## Rationale

Spring AI was chosen because it aligns with the course's educational objectives:

1. **Teaching Abstraction Principles**: Students learn to design systems that aren't tightly coupled to specific vendors
2. **Enterprise Readiness**: The patterns taught scale to real-world applications
3. **Future Extensibility**: Course can demonstrate multiple AI providers using consistent patterns
4. **Spring Ecosystem**: Leverages familiar Spring concepts (dependency injection, configuration, testing)
5. **Best Practices**: Encourages secure API key handling and proper error management

## Consequences

### Positive
- **Consistent Learning Path**: Students learn one set of patterns that work across providers
- **Real-World Relevance**: Patterns used in the course apply to production systems
- **Reduced Boilerplate**: Less code needed for common AI operations
- **Better Testing**: Easy to mock AI interactions for unit tests
- **Scalability**: Architecture supports adding new AI capabilities easily

### Negative
- **Abstraction Overhead**: Some provider-specific features may require workarounds
- **Framework Dependency**: Course tied to Spring AI's evolution and stability
- **Learning Curve**: Students must understand both AI concepts and Spring AI abstractions

### Mitigation Strategies
- **Low-Level API Access**: Later branches (13-15) demonstrate direct provider APIs when needed
- **Provider-Specific Examples**: Show how to access provider-specific features when necessary
- **Documentation**: Comprehensive explanations of when to use abstraction vs. direct access

## Implementation Guidelines

1. **Start Simple**: Begin with basic abstractions in early branches
2. **Progressive Enhancement**: Add complexity gradually (templates, JSON binding, etc.)
3. **Show Alternatives**: Demonstrate both high-level and low-level approaches
4. **Real Examples**: Use practical scenarios that students will encounter

## Review

This decision will be reviewed after Lecture 8 to assess:
- Student comprehension and feedback
- Spring AI framework maturity and stability
- Industry adoption of Spring AI patterns
- Course effectiveness in teaching AI integration concepts

## References

- [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/)
- [Spring AI GitHub Repository](https://github.com/spring-projects/spring-ai)
- [OpenAI API Documentation](https://platform.openai.com/docs)
- [Enterprise AI Integration Best Practices](https://martinfowler.com/articles/engineering-practices-llm.html)