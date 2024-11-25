public Class getGenericType(Field field) {        
    Type generic = field.getGenericType();
    if (generic != null && generic instanceof ParameterizedType) {
        Type actual = ((ParameterizedType) generic).getActualTypeArguments()[0];
        // Recursively traverse nested generics
        return getGenericType((Class<?>) actual);
    } else if (generic instanceof Class) { // Add check for direct class type
        return (Class<?>) generic;
    }
    
    return Object.class;
}