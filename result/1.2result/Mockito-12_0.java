public Class<?> getGenericType(Field field) {
    Type generic = field.getGenericType();
    if (generic != null && generic instanceof ParameterizedType) {
        Type actual = ((ParameterizedType) generic).getActualTypeArguments()[0];
        
        while (actual instanceof ParameterizedType) {
            actual = ((ParameterizedType) actual).getRawType();
        }

        return (Class<?>) actual;
    }
    
    return Object.class;
}
