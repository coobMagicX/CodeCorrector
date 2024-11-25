public <T> void resetMock(T mock) {
    MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);
    MockHandler<T> newMockHandler = new MockHandler<>(oldMockHandler);
    
    // Clear the old listeners before creating a new one.
    if (newMockHandler != null && newMockHandler.getInvocationListeners() != null) {
        for (InvocationListener listener : newMockHandler.getInvocationListeners()) {
            try {
                ((InvocationListener) listener).onClear();
            } catch (Exception e) {
                // Handle any exceptions that occur when clearing listeners
            }
        }
    }

    MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, (MockSettingsImpl) org.mockito.Mockito.withSettings().defaultAnswer(org.mockito.Mockito.RETURNS_DEFAULTS));
    ((Factory) mock).setCallback(0, newFilter);
}