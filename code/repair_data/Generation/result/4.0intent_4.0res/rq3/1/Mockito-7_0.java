private void readTypeVariables(TypeVariable<?> typeVariable) {
    for (Type type : typeVariable.getBounds()) {
        registerTypeVariablesOn(type);
    }
    registerTypeVariablesOn(getActualTypeArgumentFor(typeVariable));
}

private void registerTypeVariablesOn(Type type) {
    // Implementation of how to register type variables
}

private Type getActualTypeArgumentFor(TypeVariable<?> typeVariable) {
    // Implementation of how to get actual type argument
}