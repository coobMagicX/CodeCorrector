public Class<?> getGenericType(Field field) {
    Type genericType = field.getGenericType();
    return resolveGenericType(genericType);
}

public Class<?> resolveGenericType(Type type) {
    if (type instanceof Class) {
        return (Class<?>) type;
    }
    
    if (