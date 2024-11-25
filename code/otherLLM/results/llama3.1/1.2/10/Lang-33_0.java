public static Class<?>[] toClass(Object[] array) {
    if (array == null || array.length == 0) {
        return ArrayUtils.EMPTY_CLASS_ARRAY;
    } else {
        Class<?>[] classes = new Class[array.length];
        for (int i = 0; i < array.length; i++) {
            classes[i] = array[i].getClass();
        }
        return classes;
    }
}