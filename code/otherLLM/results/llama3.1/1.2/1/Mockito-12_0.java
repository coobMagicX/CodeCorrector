public Class getGenericType(Field field) {        
    Type generic = field.getGenericType();
    
    if (generic != null && generic instanceof ParameterizedType) {
        // Check for correct number of actual type arguments
        int actualArgumentsCount = ((ParameterizedType) generic).getActualTypeArguments().length;
        
        if (actualArgumentsCount == 1) { // Only one type argument, return it
            Type actual = ((ParameterizedType) generic).getActualTypeArguments()[0];
            return (Class) actual;
        } else {
            // Nested generics or multiple type arguments, recursively traverse the generic types
            for (Type argument : ((ParameterizedType) generic).getActualTypeArguments()) {
                if (argument instanceof ParameterizedType) {
                    Type nestedArgument = getGenericType(field);
                    return (Class) nestedArgument;
                }
            }
        }
    }
    
    // If no generic type or edge cases, return Object.class
    return Object.class;
}