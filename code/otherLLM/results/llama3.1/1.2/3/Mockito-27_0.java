public <T> void resetMock(T mock) {
    MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);
    MockHandler<T> newMockHandler = new MockHandler<T>(oldMockHandler);
    MethodInterceptorFilter newFilter = newMethodInterceptorFilter((MockSettingsImpl) org.mockito.Mockito.withSettings().defaultAnswer(org.mockito.Mockito.RETURNS_DEFAULTS));
    ((Factory) mock).setCallback(0, newFilter);

    // Notify invocation listeners about state change
    ((Factory) mock).invokeListeners(reportInvocation(mock));
}

private Invocation reportInvocation(T mock) {
    // Report invocation to associated listener(s)
    InvocationListener listener = getInvocationListener(mock);
    return new Invocation(listener.report(), listener.getInvokedMethod());
}