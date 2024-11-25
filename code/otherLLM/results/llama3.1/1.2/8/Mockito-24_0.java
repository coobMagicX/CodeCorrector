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
        if (mock == null || mock.equals(invocation.getArgument(0))) { // added check for equality
            return 0; // return 0 when comparing the mock object to itself
        } else {
            return 1;
        }
    }

    Class<?> returnType = invocation.getMethod().getReturnType();
    return returnValueFor(returnType);
}