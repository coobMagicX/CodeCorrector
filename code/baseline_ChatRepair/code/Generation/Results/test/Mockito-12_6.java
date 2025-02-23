public Class<?> getGenericType(Field field) {
    Type genericType = field.getGenericType();

    
    if (genericType instanceof ParameterizedType) {
        ParameterizedType parameterizedType = (ParameterizedType) genericType;
        Type[] typeArguments = parameterizedType.getActualTypeArguments();
        if (typeArguments.length > 0) {
            Type firstArgument = typeArguments[0];
            