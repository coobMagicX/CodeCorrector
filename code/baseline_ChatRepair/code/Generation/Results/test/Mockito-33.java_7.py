public boolean hasSameMethod(Invocation candidate) {
    Method m1 = invocation.getMethod();
    Method m2 = candidate.getMethod();

    // First level direct comparison, if both methods are the same instance.
    if (m1 == m2) {
        return true;
    }

    // Comparison of name, return type, and declaring class
    if (!m1.getName().equals(m2.getName()) || !m1.getReturnType().equals(m2.getReturnType()) || !m1.getDeclaringClass().equals(m2.getDeclaringClass())) {
        return false;
    }

    // Compare parameter types
    Class<?>[] params1 = m1.getParameterTypes();
    Class<?>[] params2 = m2.getParameterTypes();
    if (params1.length != params2.length) {
        return false;
    }

    for (int i = 0; i < params1.length; i++) {
        if (!params1[i].equals(params2[i])) {
            return false;
        }
    }

    return true;
}
