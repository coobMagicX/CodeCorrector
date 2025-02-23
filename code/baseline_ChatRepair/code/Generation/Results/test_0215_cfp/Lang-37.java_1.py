import java.lang.reflect.Array;

public static <T> T[] addAll(T[] array1, T... array2) {
    if (array1 == null) {
        return array2.clone();
    } else if (array2 == null) {
        return array1.clone();
    }
    // Deduce a common supertype to accommodate all types
    Class<?> type1 = array1.getClass().getComponentType();
    Class<?> type2 = array2.getClass().getComponentType();
    Class<?> commonSuperType = findCommonSuperclass(type1, type2);
  
    @SuppressWarnings("unchecked")
    T[] joinedArray = (T[]) Array.newInstance(commonSuperType, array1.length + array2.length);
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
        // Use Object as the common superclass if no closer common ancestor is found
        return Object.class;
    }
}
