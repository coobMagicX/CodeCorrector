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
        // Mocks should return 0 when compared to themselves.
        Object other = invocation.getArguments()[0];
        return (mock == other) ? 0 : 1; // This handles the comparison for compareTo() method by returning 0 if objects are equal.
    }
    
    Class<?> returnType = invocation.getMethod().getReturnType();
    return returnValueFor(returnType);
}