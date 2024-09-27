private void readTypeVariables() {
    Type[] bounds = typeVariable.getBounds();  // Assuming typeVariable is a TypeVariable with accessible bounds
    for (Type type : bounds) {
        registerTypeVariablesOn(type);
    }
    Type actualTypeArgument = getActualTypeArgumentFor(typeVariable);  // Assuming this method retrieves the relevant actual type argument for the typeVariable
    registerTypeVariablesOn(actualTypeArgument);
}