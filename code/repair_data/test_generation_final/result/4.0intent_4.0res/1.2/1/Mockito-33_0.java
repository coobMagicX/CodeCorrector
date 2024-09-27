public boolean hasSameMethod(Invocation candidate) {
    Method m1 = invocation.getMethod();
    Method m2 = candidate.getMethod();

    // To handle the issue with polymorphic calls and generics, enhance the check to include method parameter types
    return m1.getDeclaringClass().equals(m2.getDeclaringClass())
           && m1.getName().equals(m2.getName())
           && Arrays.equals(m1.getParameterTypes(), m2.getParameterTypes());
}