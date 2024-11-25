public <T> void resetMock(T mock) {
    MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);
    MethodInterceptorFilter newFilter = newMethodInterceptorFilter(org.mockito.Mockito.withSettings().defaultAnswer(org.mockito.Mockito.RETURNS_DEFAULTS).build());
    ((Factory) mock).setCallback(0, newFilter);
}