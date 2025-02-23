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
        // This will handle the compareTo method by the Comparable interface
        Object mock = invocation.getMock();
        Object[] args = invocation.getArguments();
        if (args != null && args.length > 0 && args[0] != null) {
            // Compare with another mock object
            if (mock == args[0]) {
                // Return 0 if the two objects are the same (self comparison)
                return 0;
            } else {
                // Simple example: return 1 if not the same. An actual comparison might require more logic.
                return 1;
            }
        }
        // Return a default value if arguments are inapplicable for comparison
        return 1;
    }

    Class<?> returnType = invocation.getMethod().getReturnType();
    return returnValueFor(returnType);
}
