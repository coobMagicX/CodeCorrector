public boolean hasSameMethod(Invocation candidate) {        
    //not using method.equals() for 1 good reason:
    //sometimes java generates forwarding methods when generics are in play see JavaGenericsForwardingMethodsTest
    Method m1 = invocation.getMethod();
    Method m2 = candidate.getMethod();
    
    /* Correct method invocation */
    return m1.equals(m2) && m1.invoke(null).getClass().isArray(); // added invoke and getClass() to check for iterator()
}

public boolean hasSameMethod(Invocation candidate) {        
    //not using method.equals() for 1 good reason:
    //sometimes java generates forwarding methods when generics are in play see JavaGenericsForwardingMethodsTest
    Method m1 = invocation.getMethod();
    Method m2 = candidate.getMethod();
    
    /* Correct method invocation */
    return m1.equals(m2) && (Iterable.class.isAssignableFrom(m1.getReturnType()) || Iterable.class.isAssignableFrom(m2.getReturnType()));
}