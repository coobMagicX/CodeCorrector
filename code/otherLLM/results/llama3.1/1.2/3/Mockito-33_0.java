public boolean hasSameMethod(Invocation candidate) {        
    Method m1 = getMethod();
    Method m2 = candidate.getMethod();
    
    	/* Avoid unnecessary cloning */
    return m1.equals(m2);
}