public <T> void resetMock(T mock) {
    if (!(mock instanceof Factory)) {
        throw new IllegalArgumentException("The provided mock must be a mock created by Mockito.");
    }

    MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);
    
    // Assuming there is a copy constructor that properly handles copying necessary settings
    MockHandler<T> newMockHandler = new MockHandler<>(oldMockHandler);
    
    // Creating a default settings based interceptor filter
    MockSettingsImpl newSettings = new MockSettingsImpl();
    newSettings.defaultAnswer(org.mockito.Mockito.RETURNS_DEFAULTS);
    MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, newSettings);
    
    // Set the new callback interceptor
    ((Factory) mock).setCallback(0, newFilter);
}
