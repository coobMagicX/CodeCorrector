public Class getGenericType(Field field) {        
    Type generic = field.getGenericType();
    if (generic != null && generic instanceof ParameterizedType) {
        // Go deep into nested generics by recursively resolving the actual type argument
        return getGenericActualClass(generic);
    }
    
    return Object.class;
}

// Helper method to recursively resolve the first actual type argument in case of parameterized types with nested generics
private Class getGenericActualClass(Type generic) {
    if (generic instanceof ParameterizedType) {
        Type[] args = ((ParameterizedType) generic).getActualTypeArguments();
        if (args.length > 0 && args[0] != null) {
            // Recursively resolve the first actual type argument
            return getGenericActualClass(args[0]);
        }
    }
    // If not a parameterized type or no actual type arguments, return it as is
    return (Class) generic;
}