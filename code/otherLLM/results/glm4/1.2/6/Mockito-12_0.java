public Class<?> getGenericType(Field field) {
    Type generic = field.getGenericType();

    // Check if the current generic type is ParameterizedType
    while (generic instanceof ParameterizedType) {
        ParameterizedType parameterizedType = (ParameterizedType) generic;
        generic = parameterizedType.getActualTypeArguments()[0]; // Get the next level of generics
    }

    // If we have reached a non-generic type, cast and return it
    if (generic instanceof Class<?>) {
        return (Class<?>) generic;
    } else if (generic instanceof TypeVariable) {
        // Handle TypeVariables or other types that cannot be directly cast to Class
        String variableName = ((TypeVariable<?>) generic).getName();
        throw new IllegalArgumentException("Generic type is a type variable: " + variableName);
    }

    // If we reach this point, the generic was null or Object.class, which should not happen in this context.
    return Object.class;
}