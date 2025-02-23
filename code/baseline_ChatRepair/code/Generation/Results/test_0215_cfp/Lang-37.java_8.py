import java.lang.reflect.Array;

@SuppressWarnings("unchecked")
public static <T> T[] addAll(T[] array1, T... array2) {
    if (array1 == null) {
        return clone(array2);
    } else if (array2 == null) {
        return clone(array1);
    }

    // Determine common base type
    Class<?> type1 = array1.getClass().getComponentType();
    Class<?> type2 = array2.getClass().getComponentType();

    // Finding the closest common super type, assuming non-primitive types for simplicity.
    // In practice, this might be more complex (handling cases like combining Integer and Double).
    Class<?> commonType = findCommonSuperType(type1, type2);
    if (commonType == null) {
        throw new IllegalArgumentException("Cannot add arrays of incompatible types: " + type1 + " and " + type2);
    }

    T[] joinedArray = (T[]) Array.newInstance(commonType, array1.length + array2.length);
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);

    return joinedArray;
}

private static Class<?> findCommonSuperType(Class<?> type1, Class<?> type2) {
    if (type1.isAssignableFrom(type2)) return type1;
    if (type2.isAssignableFrom(type1)) return type2;
    // Simplest approach: move to using Object class if no direct relation (not precise, and possibly not useful