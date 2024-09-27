public Class<?> getGenericType(Field field) {
    Type genericType = field.getGenericType();
    if (genericType instanceof ParameterizedType) {
        Type actualType = ((ParameterizedType) genericType).getActualTypeArguments()[0];
        if (actualType instanceof Class<?>) {
            return (Class<?>) actualType;
        }
    }
    return Object.class;
}