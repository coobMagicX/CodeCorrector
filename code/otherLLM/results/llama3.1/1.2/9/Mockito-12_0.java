public Class getGenericType(Field field) {        
    Type generic = field.getGenericType();
    if (generic != null && generic instanceof ParameterizedType) {
        Type actual = ((ParameterizedType) generic).getActualTypeArguments()[0];
        // Recursively traverse nested generics
        while (actual instanceof ParameterizedType) {
            actual = ((ParameterizedType) actual).getActualTypeArguments()[0];
        }
        return (Class) actual;
    }
    
    return Object.class;
}