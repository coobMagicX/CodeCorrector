public static <T> T[] addAll(T[] array1, T... array2) {
    if (array1 == null) {
        // Ensure that the cloned array has the same type as array2
        final Class<?> type2 = array2.getClass().getComponentType();
        return clone(array2, type2);
    } else if (array2 == null) {
        // Ensure that the cloned array has the same type as array1
        final Class<?> type1 = array1.getClass().getComponentType();
        return clone(array1, type1);
    }
    
    final Class<?> type1 = array1.getClass().getComponentType();
    final Class<?> type2 = array2.getClass().getComponentType();

    // Check if both arrays are of the same component type
    if (!type1.isAssignableFrom(type2)) {
        throw new IllegalArgumentException("Arrays must have compatible types.");
    }
    
    T[] joinedArray = (T[]) Array.newInstance(type1, array1.length + array2.length);
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
    return joinedArray;
}

// Helper method to clone an array with a specific type component
private static <T> T[] clone(T[] original, Class<?> componentType) {
    if (original == null) {
        return null;
    }
    T[] cloned = (T[]) Array.newInstance(componentType, original.length);
    System.arraycopy(original, 0, cloned, 0, original.length);
    return cloned;
}