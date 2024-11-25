private void readTypeVariables() {
    Type type = typeVariable.getBounds();
    if (type instanceof ParameterizedType) {
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type rawType = parameterizedType.getRawType();

        // If the raw type is a Class, get its type parameters
        if (rawType instanceof Class<?>) {
            Class<?> clazz = (Class<?>) rawType;
            TypeVariable[] typeParameters = clazz.getTypeParameters();
            
            // Register type variables on the bounds of the parameterized type
            registerTypeVariablesOn(parameterizedType);
            
            // Recursively get actual type arguments for nested generic types
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            for (int i = 0; i < actualTypeArguments.length; i++) {
                TypeVariable typeParameter = typeParameters[i];
                registerTypeVariablesOn(getActualTypeArgumentFor(typeParameter));
            }
        } else if (type instanceof WildcardType) {
            // Handle wildcard types, if necessary
            boundsOf((WildcardType) type);
        }
    } else {
        // If the type is not a parameterized or wildcard type, do nothing
    }
}

protected void registerTypeVariablesOn(ParameterizedType type) {
    Type rawType = type.getRawType();
    
    if (rawType instanceof Class<?>) {
        Class<?> clazz = (Class<?>) rawType;
        TypeVariable[] typeParameters = clazz.getTypeParameters();

        for (int i = 0; i < typeParameters.length; i++) {
            Type actualTypeArgument = type.getActualTypeArguments()[i];
            TypeVariable typeParameter = typeParameters[i];

            if (actualTypeArgument instanceof WildcardType) {
                contextualActualTypeParameters.put(typeParameter, boundsOf((WildcardType) actualTypeArgument));
            } else if (typeParameter != actualTypeArgument) {
                contextualActualTypeParameters.put(typeParameter, actualTypeArgument);
            }
        }
    }
}