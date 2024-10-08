public Class getGenericType(Field field) {        
    Type generic = field.getGenericType();
    if (generic instanceof ParameterizedType) {
        Type actual = ((ParameterizedType) generic).getActualTypeArguments()[0];
        if (actual instanceof Class) {
            return (Class) actual;
        } else if (actual instanceof ParameterizedType) {
            return (Class)((ParameterizedType) actual).getRawType();
        }
    }
    
    return Object.class;
}