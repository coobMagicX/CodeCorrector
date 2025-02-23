public boolean hasSameMethod(Invocation candidate) {
    Method m1 = invocation.getMethod();
    Method m2 = candidate.getMethod();

    // Check if the method names are the same
    if (!m1.getName().equals(m2.getName())) {
        return false; 
    }

    // Check if the methods are declared in the same class
    if (!m1.getDeclaringClass().equals(m2.getDeclaringClass())) {
        return false;
    }

    // Check if the parameter types are the same
    Class<?>[] params1 = m1.getParameterTypes();
    Class<?>[] params2 = m2.getParameterTypes();
    if (params1.length != params2.length) {
        return false;
    }

    for (int i = 0; i < params1.length; i++) {
        if (params1[i] != params2[i]) {
            return false;
        }
    }

    return true;
}
