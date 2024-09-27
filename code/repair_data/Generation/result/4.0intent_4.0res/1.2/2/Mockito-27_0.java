public <T> void resetMock(T mock) {
    MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);
    MockSettingsImpl settings = oldMockHandler.getMockSettings();
    settings.useConstructor();
    MockHandler<T> newMockHandler = new MockHandler<T>(settings);
    MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, settings);
    ((Factory) mock).setCallback(0, newFilter);
}