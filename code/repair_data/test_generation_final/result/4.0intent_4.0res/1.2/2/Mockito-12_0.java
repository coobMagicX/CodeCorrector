public Class getGenericType(Field field) {
    Type generic = field.getGenericType();
    if (generic != null && generic instanceof ParameterizedType) {
        Type actual = ((ParameterizedType) generic).getActualTypeArguments()[0];
        if (actual instanceof ParameterizedType) {
            return getOutermostType((ParameterizedType) actual);
        }
        return (Class) actual;
    }
    
    return Object.class;
}

private Class getOutermostType(ParameterizedType parameterizedType) {
    Type outermostType = parameterizedType;
    while (outermostType instanceof ParameterizedType) {
        outermostType = ((ParameterizedType) outermostType).getRawType();
    }
    return (Class) outermostType;
}