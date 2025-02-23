import java.lang.reflect.Array;

public static <T> T[] addAll(T[] array1, T[] array2) {
    if (array1 == null) {
        return cloneArray(array2);
    } else if (array2 == null) {
        return cloneArray(array1);
    }

    Class<?> type1 = array1.getClass().getComponentType();
    Class<?> type2 = array2.getClass().getComponentType();

    if (!type1.equals(type2)) {
        throw new IllegalArgumentException("Arrays have different types: " + type1 + " and " + type2);
    }

    @SuppressWarnings("unchecked")
    T[] joinedArray = (T[]) Array.newInstance(type1, array1.length + array2.length);
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);

    return joinedArray;
}

private static <T> T[] cloneArray(T[] array) {
    if (array == null) {
        return null;
    }
    return array.clone();
}
