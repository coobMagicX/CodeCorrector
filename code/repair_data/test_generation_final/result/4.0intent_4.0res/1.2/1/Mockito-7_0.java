private void readTypeVariables() {
    for (Type type : typeVariable.getBounds()) {
        registerTypeVariablesOn(type);
    }
    Type actualTypeArgument = getActualTypeArgumentFor(typeVariable);
    if (actualTypeArgument instanceof TypeVariable) {
        TypeVariable<?> dependentTypeVar = (TypeVariable<?>) actualTypeArgument;
        for (Type bound : dependentTypeVar.getBounds()) {
            registerTypeVariablesOn(bound);
        }
    }
    registerTypeVariablesOn(actualTypeArgument);
}