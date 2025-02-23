public <T> void resetMock(T mock) {
    if (!(mock instanceof Factory)) {
        throw new IllegalArgumentException("The mock must be a Mockito mock and implement the Factory interface.");
    }

    MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);
    if (oldMockHandler == null) {
        throw new IllegalStateException("Unable to get the MockHandler of the provided mock.");
    }

    // Extract settings from the old mock handler
    MockSettingsImpl settings = (MockSettingsImpl)oldMockHandler.getMockSettings();

    // Create a new MockHandler with the extracted settings
    MockHandler<T> newMockHandler = new MockHandler<T>(settings);

    // Create a new MethodInterceptorFilter with the new mock handler and the original settings
    MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, settings);

    // Set the new filter as the callback for the mock
    ((Factory) mock).setCallback(0, newFilter);
}
