// New failed test case
private void testReadTypeVariables() {
    TypeVariable<?> typeVariable = new TypeVariableImpl<>("T", Object.class, Serializable.class);
    readTypeVariables(typeVariable);
    // Assert something
}

// Fixed code
private void readTypeVariables(TypeVariable<?> typeVariable) {
    for (Type type : typeVariable.getBounds()) {
        registerTypeVariablesOn(type);
    }
    if (typeVariable instanceof ParameterizedType) {
        registerTypeVariablesOn(getActualTypeArgumentFor(typeVariable));
    }
}