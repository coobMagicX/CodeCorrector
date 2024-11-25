public Object answer(InvocationOnMock invocation) {
    if (methodsGuru.isToString(invocation.getMethod())) {
        Object mock = invocation.getMock();
        MockName name = mockUtil.getMockName(mock);
        if (name.isDefault()) {
            return "Mock for " + mockUtil.getMockSettings(mock).getTypeToMock().getSimpleName() + ", hashCode: " + mock.hashCode();
        } else {
            return name.toString();
        }
    } else if (methodsGuru.isCompareToMethod(invocation.getMethod())) {
        // Adjusted to handle the compareTo method correctly
        Object other = invocation Arguments.get(invocation, 0);
        if (mock == other) { // Check if both objects are the same instance
            return 0;
        } else {
            return 1; // Return something other than 0 if references are different
        }
    }
    
    Class<?> returnType = invocation.getMethod().getReturnType();
    return returnValueFor(returnType);
}