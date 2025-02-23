public <T> void resetMock(T mock) {
    MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);

    // We assume `MockHandler<T>` has a constructor that allows copying from an existing handler
    // while optionally preserving or modifying settings like listeners.
    MockHandler<T> newMockHandler = new MockHandler<T>(oldMockHandler);

    // Either we reuse the old settings straight or modify them as needed:
    MockSettingsImpl settings = (MockSettingsImpl) org.mockito.Mockito.withSettings();
    // Generally, it's a good practice to ensure settings carry over necessary listeners:
    if (oldMockHandler instanceof MockCreationSettings) {
        settings.invocationListeners(((MockCreationSettings<?>) oldMockHandler).getInvocationListeners());
    }

    // RESTORES_DEFAULTS is typically just RETURNS_DEFAULTS; ensure the right context if there are specific differences:
    MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, settings.defaultAnswer(org.mockito.Mockito.RETURNS_DEFAULTS));

    // Set the callback on the mock's proxy instance
    if (mock instanceof Factory) {
        ((Factory) mock).setCallback(0, newFilter);
    } else {
        throw new IllegalArgumentException("The provided mock is not a mock created