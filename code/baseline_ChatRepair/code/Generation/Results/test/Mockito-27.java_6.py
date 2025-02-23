public <T> void resetMock(T mock) {
    MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);
    // Creating a new MockHandler while transferring settings and listeners
    MockHandler<T> newMockHandler = new MockHandler<T>(oldMockHandler.getMockSettings());
    // Transfer invocation listeners if this operation is supported and needed
    if (oldMockHandler instanceof InvocationContainer) {
        List<InvocationListener> listeners = ((InvocationContainer) oldMockHandler).getListeners();
        for (InvocationListener listener : listeners) {
            newMockHandler.addListener(listener);
        }
    }
    // Setting up the new interceptor filter with default settings
    MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, (MockSettingsImpl) org.mockito.Mockito.withSettings().defaultAnswer(org.mockito.Mockito.RETURNS_DEFAULTS));
    // Updating the factory callback
    ((Factory)