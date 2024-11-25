public boolean hasSameMethod(Invocation candidate) {        
    //not using method.equals() for 1 good reason:
    //sometimes java generates forwarding methods when generics are in play see JavaGenericsForwardingMethodsTest
    Method m1 = invocation.getMethod();
    Method m2 = candidate.getMethod();

    // Compare fully qualified names to handle generics forwarding methods correctly
    return m1.getDeclaringClass().getName().equals(m2.getDeclaringClass().getName())
            && m1.getName().equals(m2.getName())
            && m1.getParameterTypes().length == m2.getParameterTypes().length;
}