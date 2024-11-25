public static Class<?>[] toClass(Object[] array) {
    if (array == null) {
        return null;
    } else if (array.length == 0) {
        return ArrayUtils.EMPTY_CLASS_ARRAY;
    }
    Class<?>[] classes = new Class[array.length];
    for (int i = 0; i < array.length; i++) {
        if (array[i] != null) {
            classes[i] = array[i].getClass();
        } else {
            // Correct the handling of null values here
            // If you want to convert null to a specific class type, replace this with it.
            // For example, if you want nulls to be treated as Class.class:
            // classes[i] = Object.class; // Example: treating nulls as Object.class
            classes[i] = null;
        }
    }
    return classes;
}