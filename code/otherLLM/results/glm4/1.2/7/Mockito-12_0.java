public Class getGenericType(Field field) {        
    Type generic = field.getGenericType();
    if (generic != null && generic instanceof ParameterizedType) {
        ParameterizedType parameterizedGeneric = (ParameterizedType) generic;

        // Check for nested generics
        while (parameterizedGeneric instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) parameterizedGeneric).getActualTypeArguments();
            if (actualTypeArguments.length > 0) {
                return (Class<?>) actualTypeArguments[0];
            }
            // Move to the next level of nesting
            Type rawType = ((ParameterizedType) parameterizedGeneric).getRawType();
            if (!rawType.equals(field.getType())) { // Ensure we don't go into infinite loops
                field = (Field) rawType; // Use reflection to get the Field object for the nested type
                generic = field.getGenericType(); // Get the generic type of the nested type
                parameterizedGeneric = (ParameterizedType) generic;
            } else {
                break; // No further nesting, exit loop
            }
        }

        // If we have reached here and there are no more actual type arguments, return Object.class
        return Object.class;
    }
    
    return Object.class;
}