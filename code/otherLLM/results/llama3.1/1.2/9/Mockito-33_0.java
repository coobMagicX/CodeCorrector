public boolean hasSameMethod(Invocation candidate) {        
    //not using method.equals() for 1 good reason:
    //sometimes java generates forwarding methods when generics are in play see JavaGenericsForwardingMethodsTest
    Method m1 = invocation.getMethod();
    Method m2 = candidate.getMethod();
    
    	/* Avoid unnecessary cloning */
    return m1.equals(m2) || 
           m1.getName().equals(m2.getName()) && 
           m1.getReturnType().equals(m2.getReturnType()) &&
           hasSameParameterTypes(m1, m2);
}

private boolean hasSameParameterTypes(Method m1, Method m2) {
    Class<?>[] p1 = m1.getParameterTypes();
    Class<?>[] p2 = m2.getParameterTypes();
    
    if (p1.length != p2.length) return false;
    
    for (int i = 0; i < p1.length; i++) {
        if (!p1[i].equals(p2[i])) return false;
    }
    
    return true;
}