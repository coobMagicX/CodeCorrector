protected void registerTypeVariablesOn(Type classType, Set<Type> processedTypes) {
    // First ensure we're dealing with a ParameterizedType and it hasn't been processed.
    if (!(classType instanceof ParameterizedType) || processedTypes.contains(classType)) {
        return;
    }
    processedTypes.add(classType);

    ParameterizedType parameterizedType = (ParameterizedType) classType;
    TypeVariable[] typeParameters = ((Class<?>) parameterizedType.getRawType()).getTypeParameters();
    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

    for (int i = 0; i < actualTypeArguments.length; i++) {
        TypeVariable typeParameter = typeParameters[i];
        Type actualTypeArgument = actualTypeArguments[i];

        if (actualTypeArgument instanceof WildcardType) {
            contextualActualTypeParameters.put(typeParameter, boundsOf((WildcardType) actualTypeArgument));
        } else {
            contextualActualTypeParameters.put(typeParameter, actualTypeArgument);
        }

        // Recursive call to handle nested ParameterizedTypes
        registerTypeVariablesOn(actualTypeArgument, processedTypes);
    }
}

// Initial call should start with an explicit type declaration for compatibility:
protected void registerTypeVariablesOn(Type classType) {
    registerTypeVariablesOn(classType, new HashSet<Type>());
}
