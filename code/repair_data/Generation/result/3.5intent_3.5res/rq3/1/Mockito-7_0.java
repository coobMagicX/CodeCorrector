private void readTypeVariables(TypeVariable<?> typeVariable) {
    for (Type type : typeVariable.getBounds()) {
        registerTypeVariablesOn(type);
    }
    registerTypeVariablesOn(getActualTypeArgumentFor(typeVariable));
}