import java.lang.reflect.Array;
import java.util.Arrays;

public static <T> T[] addAll(T[] array1, T... array2) {
    if (array1 == null) {
        return Arrays.copyOf(array2, array2.length);
    } else if (array2 == null) {
        return Arrays.copyOf(array1, array1.length);
    }
    // Ensure both arrays are of the same type based on array1
    if (!array1.getClass().getComponentType().isAssignableFrom(array2.getClass().getComponentType())) {
        throw new ArrayStoreException("Cannot add arrays of different types: " 
                                       + array1.getClass().getComponentType().getName() + " and " 
                                       + array2.getClass().getComponentType().getName());
    }
    T[] joinedArray = Arrays.copyOf(array1, array1.length + array2.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
    return joinedArray;
}
