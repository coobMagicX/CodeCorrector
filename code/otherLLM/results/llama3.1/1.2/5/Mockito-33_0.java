public boolean hasSameMethod(Invocation candidate) {        
    //not using method.equals() for 1 good reason:
    //sometimes java generates forwarding methods when generics are in play see JavaGenericsForwardingMethodsTest
    Method m1 = this.getMethod();  // Get the method from the current invocation
    Method m2 = candidate.getMethod();
    
    	/* Avoid unnecessary cloning */
    return m1.equals(m2);
}