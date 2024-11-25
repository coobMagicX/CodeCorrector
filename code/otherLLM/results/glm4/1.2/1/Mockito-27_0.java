public <T> void resetMock(T mock) {
    MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);
    if (oldMockHandler == null) {
        throw new IllegalArgumentException("No existing handler for the provided mock object.");
    }
    
    // Create a new instance of MockHandler, passing the old handler to it
    MockHandler<T> newMockHandler = new MockHandler<>(oldMockHandler);

    // Configure the settings with the default answer
    org.mockito.MockitoSettings settings = org.mockito.Mockito.withSettings()
                                                              .defaultAnswer(org.mockito.Mockito.RETURNS_DEFAULTS);
    MockSettingsImpl mockSettings = (MockSettingsImpl) settings;

    // Create a new instance of MethodInterceptorFilter, passing the new handler and the configured settings
    MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, mockSettings);

    // Retrieve the list from the mock object if possible
    List<T> mockedList = getMockedList(mock);
    if (mockedList != null) {
        // Clear the list to reset it
        mockedList.clear();

        // Assuming there is a method invocation listener that should be notified of this operation
        MethodInvocationReport report = new MethodInvocationReport();
        report.setMethod("clear");
        report.setArguments(new Object[]{});

        // Verify that the invocation listener reports the clear invocation
        verify(invocationListener).reportInvocation(any(MethodInvocationReport.class));
    }
    
    // Set the callback with the new filter to handle method invocations on the mock
    ((Factory) mock).setCallback(0, newFilter);
}