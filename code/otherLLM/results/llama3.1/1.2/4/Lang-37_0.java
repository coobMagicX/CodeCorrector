public static <T> T[] addAll(T[] array1, T... array2) {
    if (array1 == null) {
        return clone(array2);
    } else if (array2 == null) {
        return clone(array1);
    }
    final Class<?> type1 = array1.getClass().getComponentType();
    // Check for potential type mismatches between the arrays
    if (!type1.equals(array2.getClass().getComponentType())) {
        throw new RuntimeException("Incompatible types: " + type1 + " and " + array2.getClass().getComponentType());
    }
    T[] joinedArray = (T[]) Array.newInstance(type1, array1.length + array2.length);
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
    return joinedArray;
}