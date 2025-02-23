protected void registerTypeVariablesOn(Type classType, Set<Type> processedTypes) {
    // Base case: return if the type isn't parameterized or has been processed already
    if (!(classType instanceof ParameterizedType) || processedTypes.contains(classType)) {
        return;
    }
    
    // Add the current type to processed types to prevent reprocessing
    processedTypes.add(classType);
    
    ParameterizedType parameterizedType = (ParameterizedType) classType;
    TypeVariable<?>[] typeParameters = ((Class<?>) parameterizedType.getRawType()).getTypeParameters();
    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
    for (int i = 0; i < actualTypeArguments.length; i++) {
        TypeVariable<?> typeParameter = typeParameters[i];
        Type actualTypeArgument = actualTypeArguments[i];

        if (actualTypeArgument instanceof WildcardType) {
            contextualActualTypeParameters.put(typeParameter, boundsOf((WildcardType) actualTypeArgument));
        } else {
            // Check if the actualTypeArgument itself needs type variables registered
            if (actualTypeArgument instanceof ParameterizedType) {
                registerTypeVariablesOn(actualTypeArgument, processedTypes);
            }
            contextualActualTypeParameters.put(typeParameter, actualTypeArgument);
        }
    }
}

protected void registerTypeVariablesOn(Type classType) {
    Set<Type> processedTypes = new HashSet<>();
    registerTypeVariablesOn(classType, processedTypes);
}

private Type boundsOf(WildcardType wildcardType) {
    // This method should process wildcard types and return the proper bound type,
    // assuming it uses the first relevant bound for simplicity.
    Type[] upperBounds = wildcardType.getUpperBounds();
    return (upperBounds.length > 0) ? upperBounds[0] : null;
}
