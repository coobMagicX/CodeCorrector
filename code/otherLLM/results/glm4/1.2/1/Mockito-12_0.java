public Class getGenericType(Field field) {
    Type generic = field.getGenericType();
    if (generic instanceof ParameterizedType) {
        while (generic instanceof ParameterizedType) {
            actualTypeArguments = ((ParameterizedType) generic).getActualTypeArguments();
            if (actualTypeArguments != null && actualTypeArguments.length > 0) {
                generic = actualTypeArguments[0];
            } else {
                break;
            }
        }
    }

    return (Class) generic;
}