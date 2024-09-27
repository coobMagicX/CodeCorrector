private void readTypeVariables() {
    for (Type type : typeVariable.getBounds()) {
        registerTypeVariablesOn(type);
    }
    Type actualTypeArgument = getActualTypeArgumentFor(typeVariable);
    registerTypeVariablesOn(actualTypeArgument);
}