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
        // Repair strategy: Return 0 when comparing the same instance using compareTo()
        Object argument = invocation.getArguments()[0];
        if (argument == invocation.getMock()) { // Check if the argument is the same as the mock
            return 0;
        } else {
            // If they are not the same, follow the original strategy of returning a non-zero value
            return 1;
        }
    }
    
    Class<?> returnType = invocation.getMethod().getReturnType();
    return returnValueFor(returnType);
}