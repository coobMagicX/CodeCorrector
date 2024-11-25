public <T> void resetMock(T mock) {
    MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);
    if (oldMockHandler == null) {
        throw new IllegalArgumentException("The provided object is not a mockable type.");
    }

    MockHandler<T> newMockHandler = new MockHandler<>(oldMockHandler);
    MethodInterceptorFilter oldFilter = ((Factory) mock).getInterceptor();

    // Ensure that the invocation listener is set up in the new filter
    InvocationListener invocationListener = (invocation) -> {
        // Custom logic to handle the invocation can be placed here if needed.
    };

    MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, (MockSettingsImpl) org.mockito.Mockito.withSettings().defaultAnswer(org.mockito.Mockito.RETURNS_DEFAULTS), invocationListener);
    
    ((Factory) mock).setCallback(0, newFilter);

    // If there was an old filter, we need to make sure that the invocation listener is removed from it
    if (oldFilter != null && oldFilter.getInvocationListener() == invocationListener) {
        oldFilter.setInvocationListener(null);
    }
}