private void readTypeVariables(TypeVariable<?> typeVariable) {
    for (Type type : typeVariable.getBounds()) {
        registerTypeVariablesOn(type);
    }
    registerTypeVariablesOn(getActualTypeArgumentFor(typeVariable));
}

private void registerTypeVariablesOn(Type type) {
    // Implementation needed
}

private Type getActualTypeArgumentFor(TypeVariable<?> typeVariable) {
    // Implementation needed
    return null;
}