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
        Object mock = invocation.getMock();
        // If the mock is compared to itself, its compareTo method should return 0
        if (mock == null) { // Assume a null check for mock object
            return 0;
        }
        Class<?> returnType = invocation.getMethod().getReturnType();
        // For other types of comparable mocks, return their natural ordering
        return returnValueFor(returnType);
    }
    
    Class<?> returnType = invocation.getMethod().getReturnType();
    return returnValueFor(returnType);
}