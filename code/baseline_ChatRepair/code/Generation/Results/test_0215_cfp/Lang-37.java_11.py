import java.lang.reflect.Array;

public static <T> T[] addAll(T[] array1, T... array2) {
    if (array1 == null) {
        return array2.clone();
    } else if (array2 == null) {
        return array1.clone();
    }
    int array1Length = array1.length;
    int array2Length = array2.length;

    // Using Object[] to accommodate different types safely
    Object[] joinedArray = new Object[array1Length + array2Length];

    System.arraycopy(array1, 0, joinedArray, 0, array1Length);
    System.arraycopy(array2, 0, joinedArray, array1Length, array2Length);

    return (T[]) joinedArray;
}
