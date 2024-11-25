import java.lang.reflect.Array;

public static <T> T[] addAll(T[] array1, T... array2) {
    if (array1 == null) {
        return clone(array2);
    } else if (array2 == null) {
        return clone(array1);
    }
    
    final Class<?> type1 = array1.getClass().getComponentType();
    final Class<?> type2 = array2.length > 0 ? array2[0].getClass() : null;

    // Check for compatibility between the types of array1 and array2
    if (type1.isAssignableFrom(type2) && type1.isAssignableFrom(array2.getClass().getComponentType())) {
        T[] joinedArray = (T[]) Array.newInstance(type1, array1.length + array2.length);
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    } else {
        throw new IllegalArgumentException("Incompatible types: " + type1.getName() + " and " + type2.getName());
    }
}

private static <T> T[] clone(T[] array) {
    if (array == null) {
        return null;
    }
    final T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length);
    System.arraycopy(array, 0, newArray, 0, array.length);
    return newArray;
}