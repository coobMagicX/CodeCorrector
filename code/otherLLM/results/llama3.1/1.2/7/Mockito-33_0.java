public boolean hasSameMethod(Invocation candidate) {        
    //not using method.equals() for 1 good reason:
    //sometimes java generates forwarding methods when generics are in play see JavaGenericsForwardingMethodsTest
    Method m1 = invocation.getMethod();
    Method m2 = candidate.getMethod();
    
    	/* Avoid unnecessary cloning */
    return m1.equals(m2);
}

public boolean isIteratorStubbed(Iterable iterable) {
    // Stub the iterator method on the given iterable
    when(iterable.iterator()).thenReturn(null);  // Reset the iterator to null after stubbing
    try {
        // Attempt to get an iterator from the stubbed iterable
        return iterable.iterator() != null;
    } finally {
        // Reinitialize the iterator after the attempt
        when(iterable.iterator()).thenCallRealMethod();
    }
}