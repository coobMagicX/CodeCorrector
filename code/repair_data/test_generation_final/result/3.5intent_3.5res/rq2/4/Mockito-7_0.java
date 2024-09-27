private void readTypeVariables() {
    for (Type type : typeVariable.getBounds()) {
        registerTypeVariablesOn(type);
    }
    if (!(typeVariable instanceof WildcardType)) {
        registerTypeVariablesOn(getActualTypeArgumentFor(typeVariable));
    }
}