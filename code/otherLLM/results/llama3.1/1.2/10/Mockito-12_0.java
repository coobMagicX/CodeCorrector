public Class getGenericType(Field field) {        
    Type generic = field.getGenericType();
    if (generic != null && generic instanceof ParameterizedType) {
        Type actual = ((ParameterizedType) generic).getActualTypeArguments()[0];
        
        // Handle nested generics by recursively calling this method
        return getGenericType((Class) actual);
    } else if (generic instanceof Class) { 
        // Update return type handling based on actual class being retrieved
        return (Class) generic;
    }
    
    return Object.class;
}