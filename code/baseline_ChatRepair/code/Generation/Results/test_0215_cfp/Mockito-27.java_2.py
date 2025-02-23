public <T> void resetMock(T mock) {
    if (!(mock instanceof Factory)) {
        throw new IllegalArgumentException("The mock must be a Mockito mock and implement the Factory interface.");
    }

    MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);
    if (oldMockHandler == null) {
        throw new IllegalStateException("Unable to get the MockHandler of the provided mock.");
    }

    // Retrieve settings from the old mock handler which includes the original configuration such as listeners
    MockCreationSettings<T> settings = oldMockHandler.getMockSettings();

    // Create a new MockHandler with the existing settings
    MockHandler<T> newMockHandler = new MockHandler<T>(settings);

    // Reuse the MethodInterceptorFilter to ensure all settings, including listeners, are applied
    MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, settings.getDefaultAnswer());

    // Reset the mock with the new filter
    ((Factory) mock).setCallback(0, newFilter);
}
