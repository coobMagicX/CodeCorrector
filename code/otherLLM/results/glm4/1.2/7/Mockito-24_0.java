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
        // Modify the implementation to check if both references are the same object instance.
        Object other = invocation.getArguments()[0]; // Assuming that compareTo method takes one argument
        if (invocation.getMock() == other) { // Check if both references refer to the same object
            return 0;
        } else {
            // Return a non-zero value for different objects, as per original intention
            return 1;
        }
    }
    
    Class<?> returnType = invocation.getMethod().getReturnType();
    return returnValueFor(returnType);
}