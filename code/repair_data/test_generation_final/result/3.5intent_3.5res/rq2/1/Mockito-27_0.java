public <T> void resetMock(T mock) {
    MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);
    MockHandler<T> newMockHandler = new MockHandler<T>(oldMockHandler);
    MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, (MockSettingsImpl) org.mockito.Mockito.withSettings().defaultAnswer(org.mockito.Mockito.RETURNS_DEFAULTS));
    ((Factory) mock).setCallback(0, newFilter);
}

public <T> MockHandlerInterface<T> getMockHandler(T mock) {
    if (mock == null) {
        throw new NotAMockException("Argument should be a mock, but is null!");
    }

    if (isMockitoMock(mock)) {
        return (MockHandlerInterface) getInterceptor(mock).getHandler();
    } else {
        throw new NotAMockException("Argument should be a mock, but is: " + mock.getClass());
    }
}

private boolean isMockitoMock(Object mock) {
    // implementation details
    // return true if mock is a Mockito mock, otherwise false
    // implementation details
}

private Object getInterceptor(Object mock) {
    // implementation details
    // return the interceptor object for the mock
    // implementation details
}