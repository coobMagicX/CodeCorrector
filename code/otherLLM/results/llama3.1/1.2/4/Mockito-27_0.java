public <T> void resetMock(T mock) {
    MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);
    MockHandler<T> newMockHandler = new MockHandler<T>(oldMockHandler);
    
    // Correctly configure MethodInterceptorFilter to report method invocations
    MethodInterceptorFilter newFilter = newMethodInterceptorFilter(getMockSettings(mock));
    
    ((Factory) mock).setCallback(0, newFilter);
}

// New helper method to get MockSettingsImpl for a given mock object
public <T> MockSettingsImpl getMockSettings(T mock) {
    return getMockHandler(mock).getMockSettings();
}