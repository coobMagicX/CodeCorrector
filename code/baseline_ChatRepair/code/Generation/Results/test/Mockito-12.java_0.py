public Class<?> getGenericType(Field field) {        
    Type generic = field.getGenericType();
    if (generic instanceof ParameterizedType) {
        ParameterizedType parameterizedType = (ParameterizedType) generic;
        Type actual = parameterizedType.getActualTypeArguments()[0];
        if (actual instanceof Class<?>) {
            return (Class<?>) actual;
        } else if (actual instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) actual).getRawType();
        }
    }
    return Object.class; // default return value if not a Class or ParameterizedType
}
