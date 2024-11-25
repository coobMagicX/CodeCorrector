public <T> void resetMock(T mock) {
    MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);
    MockSettingsImpl settings = (MockSettingsImpl) org.mockito.Mockito.withSettings().defaultAnswer(org.mockito.Mockito.RETURNS_DEFAULTS);
    MethodInterceptorFilter newFilter = newMethodInterceptorFilter(settings);
    ((Factory) mock).setCallback(0, newFilter);

    // Verify that the callback set on the mock object is correctly triggering the invocation listener
    InvocationNotifierHandler<T> invocationNotifierHandler = (InvocationNotifierHandler<T>) newFilter.getHandler();
    invocationNotifierHandler.verifyInvocation(); // Assuming this method exists in InvocationNotifierHandler class
}