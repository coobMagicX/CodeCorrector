import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public static Object[] addAll(Object[] array1, Object... array2) {
    if (array1 == null) {
        return clone(array2);
    } else if (array2 == null) {
        return clone(array1);
    }
    Class<?> type = findCommonSuperclass(array1, array2);

    int totalLength = array1.length + array2.length;
    Object[] joinedArray = (Object[]) Array.newInstance(type, totalLength);

    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);

    return joinedArray;
}

private static Class<?> findCommonSuperclass(Object[] array1, Object[] array2) {
    List<Class<?>> classes = new ArrayList<>();
    addClasses(classes, array1);
    addClasses(classes, array2);
    
    Class<?> commonClass = classes.get(0);
    for (Class<?> clazz : classes) {
        while (!commonClass.isAssignableFrom(clazz)) {
            commonClass = commonClass.getSuperclass();
        }
    }
    return commonClass;
}

private static void addClasses(List<Class<?>> classes, Object[] array