public boolean hasSameMethod(Invocation candidate) {        
    //not using method.equals() for 1 good reason:
    //sometimes java generates forwarding methods when generics are in play see JavaGenericsForwardingMethodsTest
    Method m1 = getMethod();
    Method m2 = candidate.getMethod();
    
    	/* Avoid unnecessary cloning */
    return m1.getName().equals(m2.getName()) && 
           m1.getReturnType().equals(m2.getReturnType()) &&
           Arrays.equals(m1.getParameterTypes(), m2.getParameterTypes());
}

public boolean shouldStubbingWork() {
    Invocation invocation = new InvocationMatcher(new Invocation(Methods.iteratorMethod));
    Invocation candidate = new InvocationMatcher(invocation);
    
    return hasSameMethod(candidate) && 
           invocation.getMethod().getName().equals(candidate.getMethod().getName()) &&
           invocation.getMethod().getReturnType().equals(candidate.getMethod().getReturnType());
}