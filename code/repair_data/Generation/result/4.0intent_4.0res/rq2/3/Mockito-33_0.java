public boolean hasSameMethod(Invocation candidate) {        
    // Use .equals() to compare methods directly, ensuring they are indeed the same.
    Method m1 = invocation.getMethod();
    Method m2 = candidate.getMethod();
    
    // Check for null to avoid NullPointerException
    if (m1 == null || m2 == null) {
        return false;
    }

    // Handle cases where methods are generics or have polymorphic calls
    if (!m1.equals(m2)) {
        return m1.getName().equals(m2.getName())
               && Arrays.equals(m1.getParameterTypes(), m2.getParameterTypes())
               && m1.getReturnType().equals(m2.getReturnType());
    }

    return true;
}