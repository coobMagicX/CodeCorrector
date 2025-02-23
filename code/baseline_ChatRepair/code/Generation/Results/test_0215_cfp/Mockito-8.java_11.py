protected void registerTypeVariablesOn(Type classType) {
    Set<TypeVariable> seenTypeVariables = new HashSet<TypeVariable>();
    innerRegisterTypeVariablesOn(classType, seenTypeVariables);
}

private void innerRegisterTypeVariablesOn(Type classType, Set<TypeVariable> seenTypeVariables) {
    if (!(classType instanceof ParameterizedType)) {
        return;
    }
    ParameterizedType parameterizedType = (ParameterizedType) classType;
    TypeVariable[] typeParameters = ((Class<?>) parameterizedType.getRawType()).getTypeParameters();
    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

    for (int i = 0; i < actualTypeArguments.length; i++) {
        TypeVariable<?> typeParameter = typeParameters[i];
        Type actualTypeArgument = actualTypeArguments[i];

        // Check and add typeParameter to seenTypeVariables to prevent infinite recursion
        if (!seenTypeVariables.add(typeParameter)) {
            continue; // skip processing if this type has been seen before to prevent cyclic dependencies
        }

        try {
            if (actualTypeArgument instanceof WildcardType) {
                contextualActualTypeParameters.put(typeParameter, boundsOf((WildcardType) actualTypeArgument));
            } else {
                contextualActualTypeParameters.put(typeParameter, actualTypeArgument);
            }

            // Recursively handle nested parameterized types if not seen before and avoid recurring on same type
            if (actualTypeArgument instanceof ParameterizedType) {
                innerRegisterTypeVariablesOn(actualTypeArgument, new HashSet<TypeVariable>(seenTypeVariables));
            }
        } finally {
            // Safeguard to prevent infinite looping in circular references
            seenTypeVariables.remove(typeParameter);
        }
    }
}

private Type boundsOf(WildcardType wildcardType) {
    // Assuming this method appropriately handles the bounds of WildcardTypes
    // You may need to adjust this if this method is causing recursive calls or similar issues.
    return wildcardType.getUpperBounds()[0]; // this is a placeholder
}
