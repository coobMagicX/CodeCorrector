public static <T> T[] addAll(T[] array1, T... array2) {
    if (array1 == null) {
        return clone(array2);
    } else if (array2 == null) {
        return clone(array1);
    }
    
    final Class<?> type1 = array1.getClass().getComponentType();
    Class<?> type2 = array2.getClass().getComponentType();
    
    // Check for the same component type in both arrays
    if (!type1.isAssignableFrom(type2)) {
        throw new IllegalArgumentException("Incompatible types: " + type1.getName() + " and " + type2.getName());
    }

    T[] joinedArray = (T[]) Array.newInstance(type1, array1.length + array2.length);
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
    
    return joinedArray;
}