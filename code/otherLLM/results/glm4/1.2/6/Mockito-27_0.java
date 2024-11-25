public <T> void resetMock(T mock) {
    MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);
    MockHandler<T> newMockHandler = new MockHandler<>(oldMockHandler); // Corrected to use the correct constructor signature

    MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler,
                                                                 (MockSettingsImpl) org.mockito.Mockito.withSettings()
                                                                 .defaultAnswer(org.mockito.Mockito.RETURNS_DEFAULTS));

    if (mock instanceof Factory) {
        ((Factory) mock).setCallback(0, newFilter);
    }
}