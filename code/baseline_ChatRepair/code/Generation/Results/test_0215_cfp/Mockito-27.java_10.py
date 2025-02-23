public <T> void resetMock(T mock) {
    // Obtain the old MockHandler.
    MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);
    
    // Create a new MockHandler using the old handler's settings.
    MockSettingsImpl settings = (MockSettingsImpl) oldMockHandler.getMockSettings();
    MockHandler<T> newMockHandler = new MockHandler<>(settings);
   
    // Create a new MethodInterceptorFilter with the new MockHandler.
    MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, settings);

    // Set the new filter as callback on the mock.
    ((Factory) mock).setCallback(0, newFilter);
}
