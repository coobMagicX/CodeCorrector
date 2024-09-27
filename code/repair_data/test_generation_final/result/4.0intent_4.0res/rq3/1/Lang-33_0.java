import java.lang.reflect.Array;

public class ArrayUtils {
    public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class<?>[0];

    public static Class<?>[] toClass(Object[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return EMPTY_CLASS_ARRAY;
        }
        Class<?>[] classes = new Class<?>[array.length];
        for (int i = 0; i < array.length; i++) {
            classes[i] = array[i].getClass();
        }
        return classes;
    }
}