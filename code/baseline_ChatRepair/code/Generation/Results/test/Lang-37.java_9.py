import java.lang.reflect.Array;
import java.util.Arrays;

public static <T> T[] addAll(T[] array1, T... array2) {
    if (array1 == null) {
        return clone(array2);
    } else if (array2 == null) {
        return clone(array1);
    }
    Class<?> type1 = array1.getClass().getComponentType();
    Class<?> type2 = array2.getClass().getComponentType();
    
    // Determine the common ancestor type for type safety
    Class<?> commonType = findCommonSuperclass(type1, type2);

    @SuppressWarnings("unchecked")
    T[] joinedArray = (T[]) Array.newInstance(commonType, array1.length + array2.length);
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);

    return joinedArray;
}

private static Class<?> findCommonSuperclass(Class<?> type1, Class<?> type2) {
    // This simplistic solution assumes both types are compatibly class-loadable
    return Arrays.stream(new Class<?>[]{type1, type2})
           .reduce(Object.class, (acc, cls) -> {
               if (acc.isAssignableFrom(cls))
                   return acc;
               while (!cls.isAssignableFrom(acc)) {
                   cls = cls.getSuperclass();
               }
               return cls;
           });
}

// Placeholder clone method
public static <T> T[] clone(T[] array) {
   