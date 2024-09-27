public <T> void resetMock(T mock) {
    // Retrieve the existing mock handler
    MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);

    // Create a new MockHandler based on the settings of the old one, including listeners
    MockHandler<T> newMockHandler = new MockHandler<T>(oldMockHandler);

    // Create a new MethodInterceptorFilter with the new mock handler and preserve the settings including listeners
    MockSettingsImpl settings = (MockSettingsImpl) org.mockito.Mockito.withSettings();
    settings.defaultAnswer(org.mockito.Mockito.RETURNS_DEFAULTS);
    settings.invocationListeners(oldMockHandler.getInvocationListeners());
    MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, settings);

    // Set the new filter to the mock
    ((Factory) mock).setCallback(0, newFilter);
}