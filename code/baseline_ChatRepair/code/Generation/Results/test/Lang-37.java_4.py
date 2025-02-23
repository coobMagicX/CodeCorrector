import java.lang.reflect.Array;

public static <T> T[] addAll(T[] array1, T[] array2) {
    if (array1 == null) {
        return clone(array2);
    } else if (array2 == null) {
        return clone(array1);
    }
    // Determine the component type from the first non-null array
    Class<?> compType = array1.getClass().getComponentType();
    if (!compType.isInstance(array2.getClass().getComponentType())) {
        throw new ArrayStoreException("Array types must be compatible.");
    }
    
    @SuppressWarnings("unchecked")
    T[] joinedArray = (T[]) Array.newInstance(compType, array1.length + array2.length);
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
    return joinedArray;
}

@SuppressWarnings("unchecked")
private static <T> T[] clone(T[] array) {
    if (array == null) {
        return null;
    }
    return (T[]) array.clone();
}
