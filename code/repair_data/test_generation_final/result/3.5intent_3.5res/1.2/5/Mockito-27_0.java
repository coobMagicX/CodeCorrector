public <T> void resetMock(T mock) {
    MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);
    MethodInterceptor[] oldInterceptors = getInterceptors(mock);
    MockHandler<T> newMockHandler = new MockHandler<T>(oldMockHandler);
    MethodInterceptor[] newInterceptors = new MethodInterceptor[oldInterceptors.length];
    System.arraycopy(oldInterceptors, 0, newInterceptors, 0, oldInterceptors.length);
    newInterceptors[0] = new MethodInterceptorFilter(newMockHandler, (MockSettingsImpl) org.mockito.Mockito.withSettings().defaultAnswer(org.mockito.Mockito.RETURNS_DEFAULTS));
    ((Factory) mock).setCallbacks(newInterceptors);
}

public <T> MethodInterceptor[] getInterceptors(T mock) {
    if (mock == null) {
        throw new NotAMockException("Argument should be a mock, but is null!");
    }

    if (isMockitoMock(mock)) {
        return getInterceptor(mock).getCallbacks();
    } else {
        throw new NotAMockException("Argument should be a mock, but is: " + mock.getClass());
    }
}