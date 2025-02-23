public <T> void resetMock(T mock) {
    // Get the old mock handler which includes listeners
    MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);
    
    // Create a new MockHandler based on the old one's configurations
    MockHandler<T> newMockHandler = new MockHandler<T>(oldMockHandler);
    
    // Instantiate the new MethodInterceptorFilter
    MockSettingsImpl settings = (MockSettingsImpl) Mockito.withSettings().defaultAnswer(Mockito.RETURNS_DEFAULTS);
    
    // If the old mock handler has listeners, ensure they are transferred to the new handler
    if (oldMockHandler.getInvocationContainer().getListeners() != null) {
        newMockHandler.getInvocationContainer().setListeners(oldMockHandler.getInvocationContainer().getListeners());
    }
    
    MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler,