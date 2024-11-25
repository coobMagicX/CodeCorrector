public Class getGenericType(Field field) {        
    Type generic = field.getGenericType();
    if (generic != null && generic instanceof ParameterizedType) {
        Type actual = ((ParameterizedType) generic).getActualTypeArguments()[0];
        // Go deep to handle nested generics correctly
        while (actual instanceof ParameterizedType) {
            actual = ((ParameterizedType) actual).getActualTypeArguments()[0];
        }
        return (Class) actual;
    } else if (generic instanceof Class) { // Improve type matching and casting
        return (Class) generic;
    }
    
    return Object.class;
}