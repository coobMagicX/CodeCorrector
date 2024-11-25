private void readTypeVariables() {
    for (Type type : typeVariable.getBounds()) {
        if (type instanceof ParameterizedType) {
            registerTypeVariablesOn(((ParameterizedType) type).getActualTypeArguments());
        } else {
            registerTypeVariablesOn(type);
        }
    }
    Type actualTypeArgument = getActualTypeArgumentFor(typeVariable);
    if (actualTypeArgument != null && actualTypeArgument instanceof ParameterizedType) {
        registerTypeVariablesOn(((ParameterizedType) actualTypeArgument).getActualTypeArguments());
    } else {
        registerTypeVariablesOn(actualTypeArgument);
    }
}