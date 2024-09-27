public Class getGenericType(Field field) {        
    Type generic = field.getGenericType();
    return resolveGenericType(generic);
}

private Class resolveGenericType(Type generic) {
    if (generic instanceof ParameterizedType) {
        Type[] typeArguments = ((ParameterizedType) generic).getActualTypeArguments();
        if (typeArguments.length > 0) {
            Type actual = typeArguments[0];
            if (actual instanceof ParameterizedType) {
                return resolveGenericType(actual);
            } else if (actual instanceof Class) {
                return (Class) actual;
            }
        }
    }
    return Object.class;
}