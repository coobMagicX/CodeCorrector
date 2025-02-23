public boolean hasSameMethod(Invocation candidate) {
    Method m1 = invocation.getMethod();
    Method m2 = candidate.getMethod();

    // Check if method names are equal
    if (!m1.getName().equals(m2.getName())) {
        return false;
    }

    // Check if both methods have the same number of parameters
    Class<?>[] params1 = m1.getParameterTypes();
    Class<?>[] params2 = m2.getParameterTypes();
    if (params1.length != params2.length) {
        return false;
    }

    // Check if all parameter types are the same or compatible in the context of overriding/generics
