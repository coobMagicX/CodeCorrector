public Class<?> getGenericType(Field field) {
    Type genericType = field.getGenericType();
    
    
    while (genericType instanceof ParameterizedType) {
        Type[] actualTypeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
        if (actualTypeArguments.length > 0) {
            genericType = actualTypeArguments[0];
        } else {
            break;
        }
    }
    
    