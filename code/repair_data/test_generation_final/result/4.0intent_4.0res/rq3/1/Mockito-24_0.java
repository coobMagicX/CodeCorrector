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
        Object[] args = invocation.getArguments();
        if (args.length == 1 && args[0] == mock) {
            return 0; // Same reference, compareTo should return 0
        }
        return 1; // Different references, return non-zero value
    }

    Class<?> returnType = invocation.getMethod().getReturnType();
    return returnValueFor(returnType);
}