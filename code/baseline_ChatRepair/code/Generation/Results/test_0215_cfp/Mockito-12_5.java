public Class<?> getGenericType(Field field) {
    Type generic = field.getGenericType();
    return resolveGenericType(generic);
}

private Class<?> resolveGenericType(Type generic) {
    if (generic instanceof Class<?>) {
        return (Class<?>) generic;
    } else if (generic instanceof ParameterizedType) {
        ParameterizedType