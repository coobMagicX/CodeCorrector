private void readTypeVariables() {
    for (Type type : typeVariable.getBounds()) {
        if (type instanceof Class) {
            registerTypeVariablesOn(type);
        }
    }
    registerTypeVariablesOn(getActualTypeArgumentFor(typeVariable));
}