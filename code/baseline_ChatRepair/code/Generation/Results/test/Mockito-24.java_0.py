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
        // Adjust compareTo behavior to return 0 when comparing the same object
        Object mock = invocation.getMock();
        Object[] args = invocation.getArguments();
        if (args.length > 0 && args[0] == mock) {
            return 0; // Correct to 0 if comparing to itself
        }
        // For different objects, default behavior can be to return 1
        return 1;
    }

    Class<?> returnType = invocation.getMethod().getReturnType();
    return returnValueFor(returnType);
}
