public <T> void resetMock(T mock) {
    if (!isMockitoMock(mock)) {
        throw new NotAMockException("Argument should be a mock, but is: " + mock.getClass());
    }

    MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);
    MockHandler<T> newMockHandler = new MockHandler<T>(oldMockHandler);
    MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, (MockSettingsImpl) org.mockito.Mockito.withSettings().defaultAnswer(org.mockito.Mockito.RETURNS_DEFAULTS));
    ((Factory) mock).setCallback(0, newFilter);

    // Assume that listeners are lost during reset; reattach them if necessary
    if (mock instanceof Listenable) {
        ((Listenable) mock).registerListeners();
    }
}