public static <T> T[] addAll(T[] array1, T... array2) {
    if (array1 == null || array2 == null) {
        throw new IllegalArgumentException("Input arrays cannot be null");
    }
    
    final Class<?> type1 = array1.getClass().getComponentType();
    for (T element : array2) {
        if (!type1.isInstance(element)) {
            throw new IllegalArgumentException("Incompatible types: Cannot add elements of different type to the same array.");
        }
    }

    T[] joinedArray = (T[]) Array.newInstance(type1, array1.length + array2.length);
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);

    return joinedArray;
}