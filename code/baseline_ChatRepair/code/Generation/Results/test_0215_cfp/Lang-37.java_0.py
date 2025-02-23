import java.lang.reflect.Array;

public static <T> T[] addAll(T[] array1, T... array2) {
    if (array1 == null) {
        return array2.clone();
    } else if (array2 == null) {
        return array1.clone();
    }
    // Find component type that is compatible with both array types
    Class<?> array1Type = array1.getClass().getComponentType();
    Class<?> array2Type = array2.getClass().getComponentType();
    Class<?> commonType = findCommonSuperType(array1Type, array2Type);

    @SuppressWarnings("unchecked")
    T[] joinedArray = (T[]) Array.newInstance(commonType, array1.length + array2.length);
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
    return joinedArray;
}

private static Class<?> findCommonSuperType(Class<?> class1, Class<?> class2) {
    if (class1.isAssignableFrom(class2)) {
        return class1;
    } else if (class2.isAssignableFrom(class1)) {
        return class2;
    } else {
        // If no direct assignment possible, resort to the next compatible type (e.g., Object)
        return Object.class;
    }
}
