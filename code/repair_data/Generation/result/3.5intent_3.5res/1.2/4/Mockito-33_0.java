public boolean hasSameMethod(Invocation candidate) {
    Method m1 = invocation.getMethod();
    Method m2 = candidate.getMethod();

    // Compare method names
    if (!m1.getName().equals(m2.getName())) {
        return false;
    }

    // Compare parameter types
    Class<?>[] paramTypes1 = m1.getParameterTypes();
    Class<?>[] paramTypes2 = m2.getParameterTypes();
    if (paramTypes1.length != paramTypes2.length) {
        return false;
    }
    for (int i = 0; i < paramTypes1.length; i++) {
        if (!paramTypes1[i].equals(paramTypes2[i])) {
            return false;
        }
    }

    return true;
}