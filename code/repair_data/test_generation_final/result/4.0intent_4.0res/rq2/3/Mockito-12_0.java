public Class getGenericType(Field field) {        
    Type generic = field.getGenericType();
    while (generic instanceof ParameterizedType) {
        Type[] typeArguments = ((ParameterizedType) generic).getActualTypeArguments();
        // Assuming we need to handle only one type argument, but safely checking for empty
        if (typeArguments.length > 0) {
            generic = typeArguments[0];
        } else {
            break; // No type arguments to process
        }
    }
    
    if (generic instanceof Class) {
        return (Class) generic;
    }
    
    return Object.class;
}