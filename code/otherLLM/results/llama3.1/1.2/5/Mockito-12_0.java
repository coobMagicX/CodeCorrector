public Class getGenericType(Field field) {        
    Type generic = field.getGenericType();
    if (generic != null && generic instanceof ParameterizedType) {
        Type actual = ((ParameterizedType) generic).getActualTypeArguments()[0];
        // Recursively traverse the generic type hierarchy to handle nested generics
        return getNestedGenericType(actual);
    }
    
    return Object.class;
}

private Class getNestedGenericType(Type type) {
    if (type instanceof ParameterizedType) {
        Type[] actuals = ((ParameterizedType) type).getActualTypeArguments();
        for (Type t : actuals) {
            return getNestedGenericType(t); // Recursively call the method on each nested generic
        }
    } else if (type instanceof Class && isGenericClass((Class<?>) type)) { 
        // If it's a generic class, recursively call the method on its superclass if it exists
        Class<?> clazz = (Class<?>) type;
        if (clazz.getSuperclass() != null) {
            return getNestedGenericType(clazz.getSuperclass());
        }
    }
    
    return (Class) type; // Return the original type if no nested generics are found
}

private boolean isGenericClass(Class<?> clazz) { 
    // Helper method to check if a class has generic type arguments
    Type[] actuals = clazz.getGenericInterfaces();
    return actuals != null && actuals.length > 0;
}