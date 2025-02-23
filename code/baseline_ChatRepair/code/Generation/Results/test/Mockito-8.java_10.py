protected void registerTypeVariablesOn(Type classType) {
    Set<TypeVariable> seenTypeVariables = new HashSet<TypeVariable>();
    registerTypeVariablesOn(classType, seenTypeVariables);
}

private void registerTypeVariablesOn(Type classType, Set<TypeVariable> seenTypeVariables) {
    if (!(classType instanceof ParameterizedType)) {
        return;
    }
    ParameterizedType parameterizedType = (ParameterizedType) classType;
    TypeVariable[] typeParameters = ((Class<?>) parameterizedType.getRawType()).getTypeParameters();
    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

    for (int i = 0; i < actualTypeArguments.length; i++) {
        TypeVariable typeParameter = typeParameters[i];
        Type actualTypeArgument = actualTypeArguments[i];

        // Early return if the type variable has been processed before to avoid infinite recursion
        if (!seenTypeVariables.add(typeParameter)) {
            continue;
        }

        if (actualTypeArgument instanceof WildcardType) {
            contextualActualTypeParameters.put(typeParameter, boundsOf((WildcardType) actualTypeArgument));
        } else {
            contextualActualTypeParameters.put(typeParameter, actualTypeArgument);
        }

        // Recursively handle nested parameterized types
        if (actualTypeArgument instanceof ParameterizedType) {
            registerTypeVariablesOn(actualTypeArgument, seenTypeVariables);
        }

        // Optional logging might be re-enabled with necessary checks
        // logger.log("For '" + parameterizedType + "' found type variable : { '" + typeParameter + "(" + System.identityHashCode(typeParameter) + ")" + "' : '" + actualTypeArgument + "(" + System.identityHashCode(typeParameter) + ")" + "' }");
    }
}
