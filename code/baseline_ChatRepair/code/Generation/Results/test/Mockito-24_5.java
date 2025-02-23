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
        if (args != null && args.length == 1) {
            
            Object other = args[0];
            if (mock == other) {
                
                return 0;
            }
            
            
            return 1;
        }
        
        return 1;
   