public Class<?> getGenericType(Field field) {
    Type generic = field.getGenericType();
    if (generic != null && generic instanceof ParameterizedType) {
        Type actual = ((ParameterizedType) generic).getActualTypeArguments()[0];
        if (actual instanceof Class) {
            return (Class<?>) actual;
        }
    }

    return Object.class;
}