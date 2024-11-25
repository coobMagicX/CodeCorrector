public <T> void resetMock(T mock) {
    MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);
    if (oldMockHandler != null) { // Ensure that the old handler is not null before proceeding
        MockHandler<T> newMockHandler = new MockHandler<T>(oldMockHandler);
        MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, (MockSettingsImpl) org.mockito.Mockito.withSettings().defaultAnswer(org.mockito.Mockito.RETURNS_DEFAULTS));
        // Check if the mock is an instance of Factory before calling setCallback
        if (mock instanceof Factory) {
            ((Factory<T>) mock).setCallback(0, newFilter);
        } else {
            throw new IllegalArgumentException("Provided object must implement Factory");
        }
    } else {
        throw new IllegalStateException("No MockHandler for the provided object");
    }
}