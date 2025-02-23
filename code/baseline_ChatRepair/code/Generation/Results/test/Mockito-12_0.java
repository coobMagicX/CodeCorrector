public Class<?> getGenericType(Field field) {
    Type generic = field.getGenericType();
    
    
    while (generic instanceof ParameterizedType) {
        ParameterizedType parameterized = (ParameterizedType) generic;
        Type actualType = parameterized.getActualTypeArguments()[0];
        
        if (actualType instanceof Class<?>) {
            return (Class<?>) actualType;
        } else if (actualType instanceof ParameterizedType) {
           