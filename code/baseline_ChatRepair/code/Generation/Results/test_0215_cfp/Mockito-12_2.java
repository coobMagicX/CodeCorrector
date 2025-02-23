public Class<?> getGenericType(Field field) {
    Type generic = field.getGenericType();
    
    while (generic instanceof ParameterizedType) {
        ParameterizedType parameterizedType = (ParameterizedType) generic;
        Type[] typeArguments = parameterizedType.getActualTypeArguments();
        if (typeArguments.length > 0) {
            generic = typeArguments[0];