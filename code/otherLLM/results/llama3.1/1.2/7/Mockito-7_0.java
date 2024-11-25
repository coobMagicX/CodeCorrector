private void readTypeVariables() {
    for (Type type : typeVariable.getBounds()) {
        registerTypeVariablesOn(getActualTypeArgumentFor(type));
    }
    registerTypeVariablesOn(getActualTypeArgumentFor(typeVariable));
}