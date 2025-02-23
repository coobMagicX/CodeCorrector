protected void registerTypeVariablesOn(Type classType) {
    HashSet<Type> processedTypes = new HashSet<Type>();
    try {
        registerTypeVariablesHelper(classType, processedTypes);
    } catch (StackOverflowError e) {
        System.err.println("StackOverflowError encountered with type: " + classType);
        throw e; // rethrow to maintain error visibility
    }
}

private void registerTypeVariablesHelper(Type classType, Set<Type> processedTypes) {
    if (processedTypes.contains(classType) || !(classType instanceof ParameterizedType)) {
        return; // skip processing if type has been processed already or is not a ParameterizedType
    }

    processedTypes.add(classType);
    ParameterizedType parameterizedType = (ParameterizedType) classType;
    TypeVariable[] typeParameters = ((Class<?>) parameterizedType.getRawType()).getTypeParameters();
    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

    for (int i = 0; i < actualTypeArguments.length; i++) {
        TypeVariable<?> typeParameter = typeParameters[i];
        Type actualTypeArgument = actualTypeArguments[i];

        if (actualTypeArgument instanceof WildcardType) {
            contextualActualTypeParameters.put(typeParameter, boundsOf((WildcardType) actualTypeArgument));
        } else {
            contextualActualTypeParameters.put(typeParameter, actualTypeArgument);
        }

        // Recursively process potential ParameterizedTypes within actualTypeArguments
        if (actualTypeArgument instanceof ParameterizedType) {
            registerTypeVariablesHelper(actualTypeArgument, processedTypes);
        }
    }
    processedTypes.remove(classType);
}

private Type boundsOf(WildcardType wildcardType) {
    // Assuming a method that returns some bounds information relevant to this context
    // This should ideally handle bounds appropriately based on lower/upper bounds complexities
    return wildcardType.getUpperBounds().length > 0 ? wildcardType.getUpperBounds()[0] : wildcardType.getLowerBounds()[0];
}
