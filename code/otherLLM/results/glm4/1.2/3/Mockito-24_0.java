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
        // Repairing the compareTo implementation to return 0 when comparing identical instances
        Object argument = invocation.getArguments()[0];
        if (mock == argument) { // Assuming 'mock' is the instance being compared in the context of compareTo()
            return 0;
        } else {
            // Return a non-zero value if not the same instance to indicate inequality
            return 1;
        }
    }
    
    Class<?> returnType = invocation.getMethod().getReturnType();
    return returnValueFor(returnType);
}