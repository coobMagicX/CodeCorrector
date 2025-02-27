public static Class<?>[] toClass(Object[] array) {
    if (array == null) {
        return null;
    } else if (array.length == 0) {
        return new Class<?>[0];
    }
    Class<?>[] classes = new Class[array.length];
    for (int i = 0; i < array.length; i++) {
        if (array[i] != null) {
            classes[i] = array[i].getClass();
        } else {
            classes[i] = null;
       