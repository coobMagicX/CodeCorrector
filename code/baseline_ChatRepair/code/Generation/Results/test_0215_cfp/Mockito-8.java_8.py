protected void registerTypeVariablesOn(Type classType, Set<Type> processedTypes) {
    // Avoid processing the same type more than once due to recursive generics
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
        } else if (!(actualTypeArgument instanceof TypeVariable)) { // Avoid recursing into TypeVariables referencing themselves
            contextualActualTypeParameters.put(typeParameter, actualTypeArgument);
        }

        // Handling nested ParameterizedType to iterate deeply into their actual type arguments
        if (actualTypeArgument instanceof ParameterizedType) {
            registerTypeVariablesOn(actualTypeArgument, processedTypes);
        }
    }
}

protected void registerTypeVariablesOn(Type classType) {
    HashSet<Type> processedTypes = new HashSet<Type>(); // Explicit type for Java 5 compatibility
    registerTypeVariablesOn(classType, processedTypes);
}
