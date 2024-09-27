public Class getGenericType(Field field) {
    Type generic = field.getGenericType();
    while (generic instanceof ParameterizedType) {
        Type actual = ((ParameterizedType) generic).getActualTypeArguments()[0];
        if (actual instanceof ParameterizedType) {
            generic = actual;
        } else {
            return (Class) actual;
        }
    }
    
    if (generic instanceof Class) {
        return (Class) generic;
    }
    
    return Object.class;
}