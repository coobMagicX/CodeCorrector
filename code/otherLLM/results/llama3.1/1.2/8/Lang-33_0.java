public static Class<?>[] toClass(Object[] array) {
    if (array == null || array.length == 0) { // combine the checks into one condition
        return ArrayUtils.EMPTY_CLASS_ARRAY;
    }
    Class<?>[] classes = new Class[array.length];
    for (int i = 0; i < array.length; i++) {
        classes[i] = array[i].getClass();
    }
    return classes;
}