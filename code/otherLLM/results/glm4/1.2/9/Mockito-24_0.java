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
        // Mocks by default should return 0 if references are the same, otherwise some other value because they are not the same.
        // We need to adjust this to ensure that when comparing the same instance of a mock object using compareTo(), it returns 0.
        Object argument = invocation.getArguments()[0]; // Assuming compareTo() takes one argument
        return (mock == argument) ? 0 : 1; // Return 0 if the references are the same, otherwise 1
    }
    
    Class<?> returnType = invocation.getMethod().getReturnType();
    return returnValueFor(returnType);
}