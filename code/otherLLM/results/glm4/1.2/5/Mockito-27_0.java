public <T> void resetMock(T mock) {
    if (mock == null) {
        throw new NotAMockException("Argument should be a mock, but is null!");
    }

    MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);
    if (!isMockitoMock(mock)) {
        throw new NotAMockException("Argument should be a mock, but is: " + mock.getClass());
    }
    
    MockSettingsImpl settings = (MockSettingsImpl) org.mockito.Mockito.withSettings().defaultAnswer(org.mockito.Mockito.RETURNS_DEFAULTS);
    MockHandler<T> newMockHandler = new MockHandler<T>(oldMockHandler);
    InvocationNotifierHandler<T> invocationNotifierHandler = new InvocationNotifierHandler<T>(newMockHandler, settings);
    
    MethodInterceptorFilter newFilter = newMethodInterceptorFilter(settings);
    ((Factory) mock).setCallback(0, newFilter);
}