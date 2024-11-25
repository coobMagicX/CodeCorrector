public Class getGenericType(Field field) {
    Type generic = field.getGenericType();
    if (generic instanceof ParameterizedType) {
        while (generic instanceof ParameterizedType) {
            generic = ((ParameterizedType) generic).getActualTypeArguments()[0];
        }
        return (Class<?>) generic;
    } else {
        return Object.class;
    }
}