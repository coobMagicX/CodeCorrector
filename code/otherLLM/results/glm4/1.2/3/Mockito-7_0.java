private void readTypeVariables() {
    // Retrieve the bounds of the type variable, which includes its generic types.
    Type[] bounds = typeVariable.getBounds();
    
    // Register type variables on each bound to handle nested generic structures.
    for (Type bound : bounds) {
        registerTypeVariablesOn(bound);
    }
    
    // Get the actual type argument that corresponds to the type variable, if applicable.
    Type actualTypeArgument = getActualTypeArgumentFor(typeVariable);
    
    // Register the actual type argument if it is not null and is a valid type.
    if (actualTypeArgument != null) {
        registerTypeVariablesOn(actualTypeArgument);
    }
}

protected void registerTypeVariablesOn(Type classType) {
    if (!(classType instanceof ParameterizedType)) {
        return;
    }
    
    ParameterizedType parameterizedType = (ParameterizedType) classType;
    Type rawType = parameterizedType.getRawType();
    
    // Ensure that the raw type is a Class, which allows us to get its type parameters.
    if (!(rawType instanceof Class<?>)) {
        return;
    }

    TypeVariable[] typeParameters = ((Class<?>) rawType).getTypeParameters();
    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
    
    for (int i = 0; i < actualTypeArguments.length; i++) {
        TypeVariable typeParameter = typeParameters[i];
        Type actualTypeArgument = actualTypeArguments[i];

        if (actualTypeArgument instanceof WildcardType) {
            contextualActualTypeParameters.put(typeParameter, boundsOf((WildcardType) actualTypeArgument));
        } else if (typeParameter != actualTypeArgument) {
            contextualActualTypeParameters.put(typeParameter, actualTypeArgument);
        }
        // logger.log("For '" + parameterizedType + "' found type variable : { '" + typeParameter + "(" + System.identityHashCode(typeParameter) + ")" + "' : '" + actualTypeArgument + "(" + System.identityHashCode(typeParameter) + ")" + "' }");
    }
}