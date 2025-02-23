public <T> void resetMock(T mock) {
    MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);
    MockHandler<T> newMockHandler = new MockHandler<T>(oldMockHandler);

    // Create a new instance of the settings, employing default Mockito settings including listeners.
    MockSettings mockSettings = Mockito.withSettings();
    if (oldMockHandler instanceof MockHandler) {
        // If the oldMockHandler holds any existing settings include them in the new settings.
        mockSettings = mockSettings.defaultAnswer(((MockHandler) oldMockHandler).getDefaultAnswer());
        // Assuming there's a way to copy over listeners or similar you would handle that here.
    }

    MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, mockSettings.defaultAnswer(Mockito.RETURNS_DEFAULTS));
    ((Factory) mock).setCallback(0, newFilter);
}
