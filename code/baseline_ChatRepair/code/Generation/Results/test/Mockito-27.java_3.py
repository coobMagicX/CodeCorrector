public <T> void resetMock(T mock) {
    MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);

    // Notify the listeners before resetting the handler.
    if (oldMockHandler instanceof InvocationNotifier) {
        ((InvocationNotifier) oldMockHandler).notifyListeners();
    }

    MockHandler<T> newMockHandler = new MockHandler<T>(oldMockHandler);
    MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, (MockSettingsImpl) org.mockito.Mockito.withSettings().defaultAnswer(org.mockito.Mockito.RETURNS_DEFAULTS));
    ((Factory) mock).setCallback(0, newFilter);
}
