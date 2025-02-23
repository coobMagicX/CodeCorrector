public boolean hasSameMethod(Invocation candidate) {
    Method m1 = invocation.getMethod();
    Method m2 = candidate.getMethod();

    if (!m1.getName().equals(m2.getName())) {
        return false;  // Method names are different
    }

    if (!m1.getDeclaringClass().equals(m2.getDeclaringClass())) {
        return false;  // Declaring classes are different
    }

    if (!m1.getReturnType().equals(m2.getReturnType())) {
        return false;  // Return types are different
    }

    // Compare parameter types
    Class<?>[] params1 = m1.getParameterTypes();
    Class<?>[] params2 = m2.getParameterTypes();
    if (params1.length != params2.length) {
        return false;