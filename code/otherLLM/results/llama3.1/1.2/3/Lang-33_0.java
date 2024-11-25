public static Class<?>[] toClass(Object[] array) {
    if (array == null || allElementsNull(array)) {
        return ArrayUtils.EMPTY_CLASS_ARRAY;
    }
    Class<?>[] classes = new Class[array.length];
    for (int i = 0; i < array.length; i++) {
        classes[i] = array[i].getClass();
    }
    return classes;
}

private static boolean allElementsNull(Object[] array) {
    if (array == null || array.length == 0) {
        return true;
    }
    for (Object element : array) {
        if (element != null) {
            return false;
        }
    }
    return true;
}