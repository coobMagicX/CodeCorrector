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
        Object argument = invocation.getArguments()[0];
        if (mock == argument) {
            return 0; // If the references are the same, return 0
        } else {
            return 1; // If not, return 1 (or any other non-zero value)
        }
    }
    Class<?> returnType = invocation.getMethod().getReturnType();
    return returnValueFor(returnType);
}