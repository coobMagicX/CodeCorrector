public Class getGenericType(Field field) {        
    Type generic = field.getGenericType();
    if (generic != null && generic instanceof ParameterizedType) {
        Type[] actualTypes = ((ParameterizedType) generic).getActualTypeArguments();
        for (int i = 0; i < actualTypes.length; i++) {
            Type currentType = actualTypes[i];
            while (currentType instanceof ParameterizedType) {
                if (currentType instanceof Class<?>) {
                    return (Class<?>) currentType;
                }
                currentType = ((ParameterizedType) currentType).getActualTypeArguments()[0]; // Go deeper into nested generics
            }
            if (currentType instanceof Class<?>) {
                return (Class<?>) currentType;
            }
        }
    }

    return Object.class;
}