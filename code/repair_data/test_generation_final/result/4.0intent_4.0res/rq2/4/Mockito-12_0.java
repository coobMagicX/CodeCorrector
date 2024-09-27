public Class getGenericType(Field field) {        
    Type generic = field.getGenericType();
    while (generic instanceof ParameterizedType) {
        Type[] typeArguments = ((ParameterizedType) generic).getActualTypeArguments();
        generic = typeArguments[typeArguments.length - 1]; // Consider the last type argument in a nested scenario
    }

    if (generic instanceof Class) {
        return (Class) generic;
    }

    return Object.class;
}