public boolean hasSameMethod(Invocation candidate) {
    Method m1 = invocation.getMethod();
    Method m2 = candidate.getMethod();

    if (m1.equals(m2)) {
        return true;
    }

    // Compare declaring class, return type, method name and parameter types
    if (!m1.getDeclaringClass().equals(m2.getDeclaringClass()) ||
        !m1.getReturnType().equals(m2.getReturnType()) ||
        !m1.getName().equals(m2.getName())) {
        return false;
    }

    Class<?>[] params1 = m1.getParameterTypes();
    Class<?>[] params2 = m2.getParameterTypes();
    if (params1.length != params2.length) {
        return false;
    }

    for (int i = 0; i < params1