public boolean hasSameMethod(Invocation candidate) {
    Method m1 = getMethod();
    Method m2 = candidate.getMethod();
    
    return m1.getName().equals(m2.getName()) && Arrays.equals(m1.getParameterTypes(), m2.getParameterTypes());
}