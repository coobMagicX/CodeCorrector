public static Class<?>[] toClass(Object[] array) {
    if (array == null) {
        return null;
    } else if (array.length == 0) {
        return ArrayUtils.EMPTY_CLASS_ARRAY;
    }
    Class<?>[] classes = new Class[array.length];
    for (int i = 0; i < array.length; i++) {
        if (array[i] != null && array[i].getClass() != null) {
            classes[i] = wrappersToPrimitives(new Class<?>[]{array[i].getClass()})[0]; // Call wrapperToPrimitive
        } else if (array[i] == null || array[i].getClass() == null) { // Added this condition
            classes[i] = null; // Handle null or non-primitive type classes
        }
    }
    return classes;
}