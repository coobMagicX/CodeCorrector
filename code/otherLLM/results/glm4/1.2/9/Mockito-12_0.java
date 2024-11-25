public Class<?> getGenericType(Field field) {        
    Type generic = field.getGenericType();
    if (generic != null && generic instanceof ParameterizedType) {
        Type[] actualTypes = ((ParameterizedType) generic).getActualTypeArguments();
        for (int i = 0; i < actualTypes.length; i++) {
            Type currentType = actualTypes[i];
            while (currentType instanceof ParameterizedType) {
                currentType = ((ParameterizedType) currentType).getActualTypeArguments()[0];
            }
            return (Class<?>) currentType;
        }
    }
    
    return Object.class;
}