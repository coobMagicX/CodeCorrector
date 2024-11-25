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
        // When comparing two identical objects, the compareTo method should return 0.
        Object other = invocation.getArguments()[0];
        if (mock == other) { // Check for identity comparison
            return 0; // Return 0 when comparing the same instances
        } else {
            return 1; // Return some other value to indicate different objects
        }
    }
    
    Class<?> returnType = invocation.getMethod().getReturnType();
    return returnValueFor(returnType);
}