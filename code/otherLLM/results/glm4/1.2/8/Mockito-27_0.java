public <T> void resetMock(T mock) {
    MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);

    // Check if the original handler is null before creating a new one.
    if (oldMockHandler == null) {
        throw new NotAMockException("No valid handler associated with the provided object.");
    }

    // Create a new MockHandler that wraps the old one, ensuring it can be called properly.
    MockHandler<T> newMockHandler = new MockHandler<>(oldMockHandler);
    MethodInterceptorFilter newFilter;

    // Use the defaultAnswer from Mockito to ensure correct handling of method invocations
    org.mockito.MockitoSettings settings = org.mockito.Mockito.withSettings().defaultAnswer(org.mockito.Mockito.RETURNS_DEFAULTS);

    // Wrap the new mock handler with a filter that forwards method invocations to an invocation listener.
    newFilter = new MethodInterceptorFilter(newMockHandler, (MockSettingsImpl) settings);

    // Set the callback on the mock object using the Factory interface method that corresponds to the factory pattern.
    ((Factory) mock).setCallback(0, newFilter);
}