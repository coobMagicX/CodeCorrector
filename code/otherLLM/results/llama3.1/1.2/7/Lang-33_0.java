public static Class<?>[] toClass(Object[] array) {
    if (array == null || array.length == 0) { // Combine the conditions for better readability
        return ArrayUtils.EMPTY_CLASS_ARRAY; // Return an empty class array when input is empty or null
    }
    Class<?>[] classes = new Class[array.length];
    for (int i = 0; i < array.length; i++) {
        classes[i] = array[i].getClass();
    }
    if (!classes.equals(ArrayUtils.EMPTY_CLASS_ARRAY)) { // Check if the resulting class array is not empty before returning it
        return classes;
    } else {
        return ArrayUtils.EMPTY_CLASS_ARRAY; // Return an empty class array when all elements are null
    }
}