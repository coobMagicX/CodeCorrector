public static Class<?>[] toClass(Object[] array) {
    if (array == null) {
        return null;
    } else if (array.length == 0) {
        return new Class<?>[0]; // Correctly create an empty array of Classes
    }
    Class<?>[] classes = new Class[array.length];
    for (int i = 0; i < array.length; i++) {
        // Check if the element is null to avoid calling getClass() on null
        if (array[i] == null) {
            classes[i] = null;
        } else {
            classes[i] = array[i].getClass();
        }
    }
    return classes;
}