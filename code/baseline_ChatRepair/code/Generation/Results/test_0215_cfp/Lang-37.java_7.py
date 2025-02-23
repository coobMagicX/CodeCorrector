import java.lang.reflect.Array;
import java.util.Arrays;

@SuppressWarnings("unchecked")
public static <T> T[] addAll(T[] array1, T... array2) {
    if (array1 == null) {
        return clone(array2);
    } else if (array2 == null) {
        return clone(array1);
    }

    Class<?> type1 = array1.getClass().getComponentType();
    Class<?> type2 = array2.getClass().getComponentType();
    
    if (!type1.isAssignableFrom(type2) && !type2.isAssignableFrom(type1)) {
        throw new IllegalArgumentException("Cannot add arrays of different types: " + type1 + " and " + type2);
    }
    
    Class<?> commonType = type1.isAssignableFrom(type2) ? type2 : type1;
    T[] joinedArray = (T[]) Array.newInstance(commonType, array1.length + array2.length);
    
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
    
    return joinedArray;
}

private static <T> T[] clone(T[] array) {
    if (array == null) {
        return null;
    }
    return Arrays.copyOf(array, array.length);
}
