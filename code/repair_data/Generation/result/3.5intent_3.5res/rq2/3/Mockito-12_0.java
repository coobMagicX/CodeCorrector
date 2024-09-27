public Class<?> getGenericType(Field field) {        
    Type generic = field.getGenericType();
    while (generic instanceof ParameterizedType) {
        Type[] actualTypeArguments = ((ParameterizedType) generic).getActualTypeArguments();
        if (actualTypeArguments.length > 0) {
            Type actual = actualTypeArguments[0];
            generic = actual;
        } else {
            break;
        }
    }
    
    if(generic instanceof Class) {
        return (Class<?>) generic;
    }
    
    return Object.class;
}