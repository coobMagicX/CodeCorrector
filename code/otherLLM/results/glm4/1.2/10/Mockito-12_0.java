public Class getGenericType(Field field) {
    Type generic = field.getGenericType();
    if (generic != null && generic instanceof ParameterizedType) {
        Type[] actualTypes = ((ParameterizedType) generic).getActualTypeArguments();
        for (int i = 0; i < actualTypes.length; i++) {
            Type current = actualTypes[i];
            if (current instanceof Class<?>) {
                return (Class<?>) current;
            } else if (current instanceof ParameterizedType) {
                // In case of nested generics, we should dive deeper to find the actual class type.
                generic = ((ParameterizedType) current).getActualTypeArguments()[0];
                if (generic instanceof Class<?>) {
                    return (Class<?>) generic;
                }
            }
        }
    }
    
    return Object.class;
}