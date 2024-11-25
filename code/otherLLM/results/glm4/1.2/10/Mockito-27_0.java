public <T> void resetMock(T mock) {
    MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);
    if (oldMockHandler == null) {
        throw new NotAMockException("The provided object is not a mock.");
    }

    // Create a new MockHandler with the old one's state
    MockHandler<T> newMockHandler = new MockHandler<>(oldMockHandler);

    // Define settings that should trigger an event on method invocation
    org.mockito.MockitoSettings settings = org.mockito.Mockito.withSettings()
        .defaultAnswer(org.mockito.Mockito.RETURNS_DEFAULTS)
        .spyMode(MockitoSpyMode.ALWAYS_SPY);

    // Create a new MethodInterceptorFilter with the new MockHandler and settings
    MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, (MockSettingsImpl) settings);

    // Set the callback to the new filter on the mock object's Factory interface
    if (mock instanceof Factory) {
        ((Factory) mock).setCallback(0, newFilter);
    } else {
        throw new IllegalArgumentException("The provided object does not implement the Factory interface.");
    }
}