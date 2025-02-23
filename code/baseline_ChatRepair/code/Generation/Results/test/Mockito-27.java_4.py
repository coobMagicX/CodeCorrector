public <T> void resetMock(T mock) {
    MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);
    MockHandler<T> newMockHandler = new MockHandler<T>(oldMockHandler);

    // Since there doesn't seem to be a direct `InvocationNotifier`, we try to replicate or carry forward the necessary settings.
    MockSettingsImpl settings = (MockSettingsImpl) org.mockito.Mockito.withSettings();
    if (oldMockHandler instanceof MockitoMockHandler) {
        settings.invocationListeners(((MockitoMockHandler) oldMockHandler).getInvocationListeners());
    }

    MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, settings.defaultAnswer(org.mockito.Mockito.RETURNS_DEFAULTS));
    ((Factory) mock).setCallback(0, newFilter);
}
