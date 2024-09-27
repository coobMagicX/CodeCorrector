public Class getGenericType(Field field) {        
    Type generic = field.getGenericType();
    while (generic instanceof ParameterizedType) {
        Type actual = ((ParameterizedType) generic).getActualTypeArguments()[0];
        if (actual instanceof ParameterizedType) {
            generic = actual;
        } else if (actual instanceof Class) {
            return (Class) actual;
        } else {
            return Object.class;
        }
    }
    
    return Object.class;
}