public <T> void resetMock(T mock) {
    MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);
    MockHandler<T> newMockHandler = new MockHandler<T>(oldMockHandler);
    
    // Ensure the new filter is set up to listen for method invocations
    MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, (MockSettingsImpl) org.mockito.Mockito.withSettings().defaultAnswer(org.mockito.Mockito.RETURNS_DEFAULTS));
    
    // Register the listener with the correct event or method in MockHandlerInterface to trigger reporting on MethodInvocationReport
    if (oldMockHandler != null) {
        oldMockHandler.unregisterInvocationListener();
        newMockHandler.registerInvocationListener(new InvocationListener() {
            @Override
            public void report(MethodInvocationReport report) {
                // Code to handle the reported MethodInvocation
                System.out.println("MethodInvoked: " + report.getMethodName());
            }
        });
    }
    
    ((Factory) mock).setCallback(0, newFilter);
}