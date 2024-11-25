public Class getGenericType(Field field) {
    if (field == null) {
        return Object.class;
    }

    Type generic = field.getGenericType();
    if (generic != null && generic instanceof ParameterizedType) {
        Type[] actualTypeArguments = ((ParameterizedType) generic).getActualTypeArguments();

        for (int i = 0; i < actualTypeArguments.length; i++) {
            Type currentType = actualTypeArguments[i];
            if (currentType instanceof Class) {
                return (Class<?>) currentType;
            } else if (currentType instanceof ParameterizedType) {
                generic = ((ParameterizedType) currentType).getRawType();
                while (generic instanceof ParameterizedType) {
                    Type[] nestedArgs = ((ParameterizedType) generic).getActualTypeArguments();
                    for (int j = 0; j < nestedArgs.length; j++) {
                        if (nestedArgs[j] instanceof Class) {
                            return (Class<?>) nestedArgs[j];
                        }
                    }
                    // If the current type is a ParameterizedType, get its raw type and continue
                    generic = ((ParameterizedType) generic).getRawType();
                }
            }
        }
    }

    return Object.class;
}