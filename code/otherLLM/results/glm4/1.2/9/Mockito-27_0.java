public <T> void resetMock(T mock) {
    MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);
    if (oldMockHandler == null) {
        throw new NotAMockException("No handler found for the provided mock.");
    }

    MockHandler<T> newMockHandler = new MockHandler<>(oldMockHandler);
    MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, (MockSettingsImpl) org.mockito.Mockito.withSettings().defaultAnswer(org.mockito.Mockito.RETURNS_DEFAULTS));

    // Assuming 'setCallback' is the method that sets up invocation listeners for method invocations.
    // Replace '0' with a placeholder value or remove it if it's not used since there is no context provided about its purpose.
    ((Factory) mock).setCallback(newFilter);
}