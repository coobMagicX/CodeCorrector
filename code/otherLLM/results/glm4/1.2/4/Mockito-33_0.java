public boolean hasSameMethod(Invocation candidate) {
    // Use equals to compare the methods, ensuring that method identity is correct.
    return (invocation.getMethod() == null ? candidate.getMethod() == null : invocation.getMethod().equals(candidate.getMethod()));
}