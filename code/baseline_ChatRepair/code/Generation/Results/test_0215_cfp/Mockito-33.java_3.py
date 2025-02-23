public boolean hasSameMethod(Invocation candidate) {
    Method m1 = invocation.getMethod();
    Method m2 = candidate.getMethod();

    if (!m1.getName().equals(m2.getName())) {
        return false; // Different method names
    }

    // Compare that the declaring class in terms of the runtime class or interface they belong to are equal
    if (!m1.getDeclaringClass().equals(m2.getDeclaringClass())) {
        return false;
    }

    // Compare parameter types
    Class<?>[] params1 = m1.getParameterTypes();
    Class<?>[] params2 = m2.getParameterTypes();

    if (params1.length != params2.length) {
        return false; //