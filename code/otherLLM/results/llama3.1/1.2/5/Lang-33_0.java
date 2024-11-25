public static Class<?>[] toClass(Object[] array) {
    if (array == null || array.length == 0) { // Combine null and zero-length checks into a single condition
        return ArrayUtils.EMPTY_CLASS_ARRAY;
    }
    Class<?>[] classes = new Class[array.length];
    for (int i = 0; i < array.length; i++) {
        if (array[i] != null) { // Add null handling by checking each element before assigning its class
            classes[i] = array[i].getClass();
        } else {
            classes[i] = null; // Assign null to the output array for null elements in the input array
        }
    }
    return classes;
}