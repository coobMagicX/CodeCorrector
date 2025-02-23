public <T> void resetMock(T mock) {
    if (mock == null) {
        throw new IllegalArgumentException("Mock cannot be null");
    }
    
    // Ensure mock is a Mockito mock and supports the necessary operations
    if (!(mock instanceof Factory)) {
        throw new IllegalArgumentException("Given object is not a mock or does not support required operations");
    }
    
    try {
        MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);
        if (oldMockHandler == null) {
            throw new IllegalStateException("Failed to retrieve the current mock handler");
        }
        
        MockSettingsImpl mockSettings = oldMockHandler.getMockSettings();
        MockHandler<T> newMockHandler = new MockHandler<T>(mockSettings);
        
        MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, mockSettings);
        
        // Set the new callback interceptor on the mock
        ((Factory) mock).setCallback(0, newFilter);
    } catch (Exception e) {
        // Log and rethrow exception to understand what went wrong
        // Consider using your logging approach here. For example:
        System.err.println