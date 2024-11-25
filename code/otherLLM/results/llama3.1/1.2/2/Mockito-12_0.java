public Class getGenericType(Field field) {        
    Type generic = field.getGenericType();
    if (generic != null && generic instanceof ParameterizedType) {
        Type actual = ((ParameterizedType) generic).getActualTypeArguments()[0];
        // Recursively handle nested generics by calling getGenericType() on the actual type
        return (Class) getGenericType(field, actual);
    }
    
    return Object.class;
}

// Helper method to recursively retrieve the generic type of a field
private Class getGenericType(Field field, Type type) {
    if (!(type instanceof ParameterizedType)) {
        // If it's not a parameterized type, just return its class
        return (Class) type;
    }
    
    Type[] actualTypes = ((ParameterizedType) type).getActualTypeArguments();
    // Recursively call getGenericType() on each actual type argument
    for (Type actual : actualTypes) {
        Class genericType = getGenericType(field, actual);
        if (genericType != null) {
            return genericType;
        }
    }
    
    return Object.class;
}