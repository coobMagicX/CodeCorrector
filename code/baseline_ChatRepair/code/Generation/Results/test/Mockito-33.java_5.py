public boolean hasSameMethod(Invocation candidate) {
    Method m1 = invocation.getMethod();
    Method m2 = candidate.getMethod();

    // Check if both methods have the same name
    if (!m1.getName().equals(m2.getName())) {
        return false;
    }

    // Check if the parameter types are the same
    if (!Arrays.equals(m1.getParameterTypes(), m2.getParameterTypes())) {
        return false;
    }

    // Check if the methods are declared by the same class or if one declares the other
    if (!m1.getDeclaringClass().isAssignableFrom(m2.getDeclaringClass()) 
            && !m2.getDeclaringClass().isAssignableFrom(m1.getDeclaringClass())) {
        return false