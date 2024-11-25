public Class getGenericType(Field field) {        
    Type generic = field.getGenericType();
    if (generic != null && generic instanceof ParameterizedType) {
        Type[] actualArgs = ((ParameterizedType) generic).getActualTypeArguments();
        // Iterate over the type arguments to handle nested generics
        for (int i = 0; i < actualArgs.length; i++) {
            if (!(actualArgs[i] instanceof Class)) {
                // If it's not a Class, it must be another ParameterizedType
                Type currentNested = actualArgs[i];
                while (currentNested instanceof ParameterizedType) {
                    currentNested = ((ParameterizedType) currentNested).getActualTypeArguments()[0];
                }
                return (Class<?>) currentNested;
            } else {
                // If it's a Class, we have reached the outermost generic type
                return (Class<?>) actualArgs[i];
            }
        }
    }
    
    // If no ParameterizedType found or only raw types are present
    return Object.class;
}