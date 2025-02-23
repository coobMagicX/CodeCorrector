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

        
        if (args != null && args.length > 0 && mock == args[0]) {
            
            return 0;
        } else {
            
            return 1;
        }
    }

    Class<?> returnType = invocation.getMethod().getReturnType();
    
    return