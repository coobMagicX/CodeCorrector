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
        // Handle compareTo method as per Comparable interface
        Object mock = invocation.getMock();
        Object[] args = invocation.getArguments();
        if (args == null || args.length == 0) {
            // No arguments passed to compareTo, return default non-zero value.
            return 1;
        } else {
            Object other = args[0];
            // When the object is compared to itself, it should return 0.
            if (mock == other) {
                return 0;
            } else {
                return mock.hashCode() > other.hashCode() ? 1 : -1; // Just a simplistic comparison for different mocks.
            }
        }
    }

    // For methods that are not toString or compareTo
    Class<?> returnType = invocation.getMethod().getReturnType();
    return returnValueFor(returnType);
}
