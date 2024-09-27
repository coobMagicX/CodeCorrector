private void readTypeVariables() {
    if (typeVariable.getBounds().length != 0) {
        for (Type type : typeVariable.getBounds()) {
            registerTypeVariablesOn(type);
        }
    }
    registerTypeVariablesOn(getActualTypeArgumentFor(typeVariable));
}

private void registerTypeVariablesOn(Type type) {
    // logic to register type variables
}

private Type getActualTypeArgumentFor(TypeVariable typeVariable) {
    // logic to get actual type argument for type variable
}