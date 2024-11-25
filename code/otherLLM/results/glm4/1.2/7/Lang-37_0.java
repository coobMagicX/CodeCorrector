public static <T> T[] addAll(T[] array1, T... array2) {
    if (array1 == null) {
        return clone(array2);
    } else if (array2 == null) {
        return clone(array1);
    }

    final Class<?> type1 = array1.getClass().getComponentType();
    final Class<?> type2 = array2.getClass().getComponentType();

    // Check for compatible types, since the generic type T cannot be a primitive
    if (!type1.isPrimitive() && !type2.isPrimitive()) {
        if (type1 != type2) {
            throw new IllegalArgumentException("Incompatible types: " + type1.getSimpleName() + " and " + type2.getSimpleName());
        }
    } else if (type1.isPrimitive() && type2.isPrimitive()) {
        // If both are primitive, check for specific primitive types
        if ((type1 == Double.class || type1 == Float.class) && (type2 == Integer.class || type2 == Long.class)) {
            throw new IllegalArgumentException("Incompatible primitive types: " + type1.getSimpleName() + " and " + type2.getSimpleName());
        }
    } else if (!type1.isPrimitive() && type2.isPrimitive()) {
        // If one is not a primitive and the other is, they cannot be combined
        throw new IllegalArgumentException("Incompatible types: " + type1.getSimpleName() + " and " + type2.getSimpleName());
    }

    T[] joinedArray = (T[]) Array.newInstance(type1, array1.length + array2.length);
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);

    return joinedArray;
}