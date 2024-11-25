public Class getGenericType(Field field) {        
    Type generic = field.getGenericType();
    if (generic != null && generic instanceof ParameterizedType) {
        Type actual = ((ParameterizedType) generic).getActualTypeArguments()[0];
        // Recursively handle nested generics by calling getGenericType on the actual type
        return getGenericType((Class) actual);
    }
    
    return Object.class;
}