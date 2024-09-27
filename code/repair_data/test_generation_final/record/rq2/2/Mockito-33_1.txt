public boolean hasSameMethod(Invocation candidate) {
    //not using method.equals() for 1 good reason:
    //sometimes java generates forwarding methods when generics are in play see JavaGenericsForwardingMethodsTest
    Method m1 = invocation.getMethod();
    Method m2 = candidate.getMethod();

    // Check both methods for null to avoid NullPointerException
    if (m1 == null || m2 == null) {
        return false;
    }

    // Compare the method names and parameter types to ensure they are the same
    if (!m1.getName().equals(m2.getName())) {
        return false;
    }

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