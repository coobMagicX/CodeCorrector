private void readTypeVariables() {
    for (Type type : typeVariable.getBounds()) {
        if (type instanceof TypeVariable) {
            registerTypeVariablesOn(type);
        }
    }
    Type actualTypeArgument = getActualTypeArgumentFor(typeVariable);
    if (actualTypeArgument instanceof TypeVariable) {
        registerTypeVariablesOn(actualTypeArgument);
    }
}