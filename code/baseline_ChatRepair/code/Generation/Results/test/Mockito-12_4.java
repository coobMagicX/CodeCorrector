public Class<?> getGenericType(Field field) {        
    Type genericType = field.getGenericType();
    
    while (genericType instanceof ParameterizedType) {
        ParameterizedType paramType = (ParameterizedType) genericType;
        Type[] typeArguments = paramType.getActualTypeArguments();
        if (typeArguments.length > 0) {
            Type firstArgument = type