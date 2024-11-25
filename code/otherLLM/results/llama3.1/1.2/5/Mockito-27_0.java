public <T> void resetMock(T mock) {
    MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);
    MockHandler<T> newMockHandler = new MockHandler<T>(oldMockHandler);
    MethodInterceptorFilter newFilter = createMethodInterceptorFilter(newMockHandler, (MockSettingsImpl) org.mockito.Mockito.withSettings().defaultAnswer(org.mockito.Mockito.RETURNS_DEFAULTS));
    ((Factory) mock).setCallback(0, newFilter);
}

private <T> MethodInterceptorFilter createMethodInterceptorFilter(MockHandler<T> handler, MockSettingsImpl settings) {
    return new MethodInterceptorFilter(handler, settings);
}