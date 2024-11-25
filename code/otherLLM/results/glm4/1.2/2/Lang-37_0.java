import java.lang.reflect.Array;

public static <T> T[] addAll(T[] array1, T... array2) {
    if (array1 == null) {
        return clone(array2);
    } else if (array2 == null) {
        return clone(array1);
    }
    
    final Class<?> type1 = array1.getClass().getComponentType();
    final Class<?> type2 = array2.getClass().getComponentType();
    
    // Check for type compatibility
    if (!type1.isAssignableFrom(type2)) {
        throw new IllegalArgumentException("Incompatible types: " + type1.getName() + " and " + type2.getName());
    }
    
    T[] joinedArray = (T[]) Array.newInstance(type1, array1.length + array2.length);
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
    
    return joinedArray;
}

private static <T> T[] clone(T[] array) {
    final Class<?> type = array.getClass().getComponentType();
    @SuppressWarnings("unchecked")
    T[] clonedArray = (T[]) Array.newInstance(type, array.length);
    System.arraycopy(array, 0, clonedArray, 0, array.length);
    return clonedArray;
}