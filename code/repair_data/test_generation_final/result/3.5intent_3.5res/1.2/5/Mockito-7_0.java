private void readTypeVariables() {
    for (Type type : typeVariable.getBounds()) {
        registerTypeVariablesOn(type);
    }
    Type actualTypeArgument = getActualTypeArgumentFor(typeVariable);
    if (actualTypeArgument instanceof ParameterizedType) {
        registerTypeVariablesOn(actualTypeArgument);
    }
}