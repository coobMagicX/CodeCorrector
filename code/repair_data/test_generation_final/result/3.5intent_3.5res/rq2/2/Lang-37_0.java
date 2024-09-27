public static <T> T[] addAll(T[] array1, T... array2) {
    if (array1 == null) {
        return clone(array2);
    } else if (array2 == null) {
        return clone(array1);
    }
    final Class<?> type1 = array1.getClass().getComponentType();
    final Class<?> type2 = array2.getClass().getComponentType();
    if (!type1.isAssignableFrom(type2) && !type2.isAssignableFrom(type1)) {
        throw new IllegalArgumentException("Cannot add arrays of different types");
    }
    T[] joinedArray = (T[]) Array.newInstance(type1, array1.length + array2.length);
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
    return joinedArray;
}

public static Boolean[] toObject(boolean[] array) {
    if (array == null) {
        return null;
    } else if (array.length == 0) {
        return EMPTY_BOOLEAN_OBJECT_ARRAY;
    }
    final Boolean[] result = new Boolean[array.length];
    for (int i = 0; i < array.length; i++) {
        result[i] = (array[i] ? Boolean.TRUE : Boolean.FALSE);
    }
    return result;
}