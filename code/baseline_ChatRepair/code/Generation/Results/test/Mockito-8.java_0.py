protected void registerTypeVariablesOn(Type classType, Set<Type> processedTypes) {
    if (!(classType instanceof ParameterizedType) || processedTypes.contains(classType)) {
        return;
    }
    
    // Add the current type to the set of processed types to avoid infinite recursion
    processedTypes.add(classType);
    
    ParameterizedType parameterizedType = (ParameterizedType) classType;
    TypeVariable<?>[] typeParameters = ((Class<?>) parameterizedType.getRawType()).getTypeParameters();
    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
    for (int i = 0; i < actualTypeArguments.length; i++) {
        TypeVariable<?> typeParameter = typeParameters[i];
        Type actualTypeArgument = actualTypeArguments[i];

        if (actualTypeArgument instanceof WildcardType) {
            contextualActualTypeParameters.put(typeParameter, boundsOf((WildcardType) actualTypeArgument));
        } else if (actualTypeArgument instanceof ParameterizedType) {
            // Recursively handle nested parameterized types
            registerTypeVariablesOn(actualTypeArgument, processedTypes);
            contextualActualTypeParameters.put(typeParameter, actualTypeArgument);
        } else {
            contextualActualTypeParameters.put(typeParameter, actualTypeArgument);
        }
    }
}

protected void registerTypeVariablesOn(Type classType) {
    registerTypeVariablesOn(classType, new HashSet<>());
}
