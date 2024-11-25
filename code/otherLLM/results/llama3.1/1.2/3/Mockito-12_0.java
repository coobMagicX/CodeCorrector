public Class getGenericType(Field field) {        
    Type generic = field.getGenericType();
    if (generic != null && generic instanceof ParameterizedType) {
        Type actual = ((ParameterizedType) generic).getActualTypeArguments()[0];
        // Implement recursive approach to handle nested generics
        return getRecursiveGenericType((Class) actual);
    }
    
    return Object.class;
}

// Helper method for recursive handling of nested generics
public Class getRecursiveGenericType(Class type) {
    while (true) {
        Type generic = type.getGenericType();
        if (generic != null && generic instanceof ParameterizedType) {
            Type actual = ((ParameterizedType) generic).getActualTypeArguments()[0];
            return getRecursiveGenericType((Class) actual);
        } else {
            break;
        }
    }
    return type;
}