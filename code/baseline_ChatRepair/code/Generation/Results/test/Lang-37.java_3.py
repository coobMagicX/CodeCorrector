import java.lang.reflect.Array;

public static <T> T[] addAll(T[] array1, T... array2) {
    if (array1 == null) {
        return array2.clone();
    } else if (array2 == null) {
        return array1.clone();
    }
    Class<?> type1 = array1.getClass().getComponentType();
    Class<?> type2 = array2.getClass().getComponentType();
    Class<?> commonType = findCommonSuperclass(type1, type2);

    @SuppressWarnings("unchecked")
    T[] joinedArray = (T[]) Array.newInstance(commonType, array1.length + array2.length);
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
    return joinedArray;
}

private static Class<?> findCommonSuperclass(Class<?> class1, Class<?> class2) {
    if (class1.isAssignableFrom(class2)) {
        return class1;
    } else if (class2.isAssignableFrom(class1)) {
        return class2;
    } else {
        return Object.class; // in case of no commonality, fallback to Object
    }
}
