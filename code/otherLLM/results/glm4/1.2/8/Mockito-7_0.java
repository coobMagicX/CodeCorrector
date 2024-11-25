private void readTypeVariables() {
    for (Type type : typeVariable.getBounds()) {
        if (!(type instanceof TypeVariable)) continue; // Skip non-TypeVariable types

        registerTypeVariablesOn(type);
        TypeVariable<?> tv = (TypeVariable<?>) type;

        if (tv.getLowerBound() != Object.class && tv.getUpperBound() != null) {
            // If the bounds are not just 'Object', there might be nested generics to consider.
            readTypeVariables(); // Recursive call for nested generics
        }
    }

    registerTypeVariablesOn(getActualTypeArgumentFor(typeVariable()));
}

protected void registerTypeVariablesOn(Type classType) {
    if (!(classType instanceof ParameterizedType)) {
        return;
    }
    ParameterizedType parameterizedType = (ParameterizedType) classType;
    TypeVariable[] typeParameters = ((Class<?>) parameterizedType.getRawType()).getTypeParameters();
    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

    for (int i = 0; i < actualTypeArguments.length; i++) {
        TypeVariable<?> typeParameter = typeParameters[i];
        Type actualTypeArgument = actualTypeArguments[i];

        if (actualTypeArgument instanceof WildcardType) {
            contextualActualTypeParameters.put(typeParameter, boundsOf((WildcardType) actualTypeArgument));
        } else if (typeParameter != actualTypeArgument) {
            contextualActualTypeParameters.put(typeParameter, actualTypeArgument);
        }
    }
}