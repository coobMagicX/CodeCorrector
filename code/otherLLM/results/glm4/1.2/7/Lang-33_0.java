public static Class<?>[] toClass(Object[] array) {
    if (array == null) {
        return null;
    } else if (array.length == 0) {
        return ArrayUtils.EMPTY_CLASS_ARRAY;
    }

    Class<?>[] classes = new Class[array.length];
    for (int i = 0; i < array.length; i++) {
        Object element = array[i];
        if (element != null) {
            classes[i] = element.getClass();
        } else {
            // Handle the case where an object is null to ensure it does not cause an exception
            classes[i] = null;
        }
    }

    return classes;
}