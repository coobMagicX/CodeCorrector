public Class getGenericType(Field field) {
    Type generic = field.getGenericType();
    if (generic != null && generic instanceof ParameterizedType) {
        while (generic instanceof ParameterizedType) { // Loop until we reach a non-ParameterizedType
            generic = ((ParameterizedType) generic).getActualTypeArguments()[0];
        }
        return (Class) generic;
    }

    return Object.class;
}