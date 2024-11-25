public static <T> T[] addAll(T[] array1, T... array2) {
    if (array1 == null || array2 == null) {
        throw new NullPointerException("Both arrays must be non-null");
    }
    
    Class<?> type1 = array1.getClass().getComponentType();
    for (Object obj : array2) {
        if (!type1.isInstance(obj)) {
            throw new IllegalArgumentException("Incompatible types: " + type1.getName() + " and " + obj.getClass().getName());
        }
    }

    T[] joinedArray = (T[]) Array.newInstance(type1, array1.length + array2.length);
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
    return joinedArray;
}