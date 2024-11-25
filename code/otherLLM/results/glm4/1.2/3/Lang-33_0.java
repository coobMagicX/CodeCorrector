public static Class<?>[] toClass(Object[] array) {
    if (array == null) {
        return null;
    } else if (array.length == 0) {
        return ArrayUtils.EMPTY_CLASS_ARRAY;
    }
    Class<?>[] classes = new Class[array.length];
    for (int i = 0; i < array.length; i++) {
        // Check if the object is null and handle it accordingly
        if (array[i] == null) {
            classes[i] = Object.class; // Assign a default class type for null objects
        } else {
            classes[i] = array[i].getClass();
        }
    }
    return classes;
}