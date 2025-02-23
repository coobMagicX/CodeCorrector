protected void registerTypeVariablesOn(Type classType, Set<Type> processedTypes) {
    if (processedTypes.contains(classType) || !(classType instanceof ParameterizedType)) {
        return; // Skip processing if type has been processed already or is not a ParameterizedType
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
    }

    // Clear the processedType for recursive calls to potentially process the same type in different contexts
    processedTypes.remove(classType);
}

protected void registerTypeVariablesOn(Type classType) {
    registerTypeVariablesOn(classType, new HashSet<>());
}
