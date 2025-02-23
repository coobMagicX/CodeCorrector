public Class<?> getGenericType(Field field) {
    Type genericType = field.getGenericType();

    
    while (genericType instanceof ParameterizedType) {
        ParameterizedType parameterizedType = (ParameterizedType) genericType;
        Type[] typeArguments = parameterizedType.getActualTypeArguments();

        
        if (typeArguments.length > 0) {
            genericType = typeArguments[0];
