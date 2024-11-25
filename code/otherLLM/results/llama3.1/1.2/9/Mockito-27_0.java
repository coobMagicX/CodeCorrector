public <T> void resetMock(T mock) {
    MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);
    MockHandler<T> newMockHandler = new MockHandler<T>(oldMockHandler);
    
    // Set the callback with the correct method interceptor filter
    MethodInterceptorFilter newFilter = createMethodInterceptorFilter(newMockHandler, org.mockito.Mockito.withSettings().defaultAnswer(org.mockito.Mockito.RETURNS_DEFAULTS));
    ((Factory) mock).setCallback(0, newFilter);
}

// New method to create the correct method interceptor filter
private <T> MethodInterceptorFilter createMethodInterceptorFilter(MockHandler<T> handler, MockSettingsImpl settings) {
    return new MethodInterceptorFilter(handler, settings);
}