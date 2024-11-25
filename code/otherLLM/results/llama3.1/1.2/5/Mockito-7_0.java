private void readTypeVariables() {
    for (Type type : typeVariable.getBounds()) {
        registerTypeVariablesOn(type);
    }
    
    // Check if the actual type argument of the type variable is a ParameterizedType, 
    // which would indicate a generic return type with nested classes and methods.
    Type actualTypeArgument = getActualTypeArgumentFor(typeVariable);
    if (actualTypeArgument instanceof ParameterizedType) {
        // Recursively iterate over the type variables in the nested classes and methods
        for (Type nestedType : ((ParameterizedType) actualTypeArgument).getActualTypeArguments()) {
            registerTypeVariablesOn(nestedType);
        }
    } else {
        registerTypeVariablesOn(actualTypeArgument);
    }
}