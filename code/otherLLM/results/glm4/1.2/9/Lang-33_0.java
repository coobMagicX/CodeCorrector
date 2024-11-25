public static Class<?>[] toClass(Object[] array) {
    if (array == null) {
        return null;
    } else if (array.length == 0) {
        return ArrayUtils.EMPTY_CLASS_ARRAY;
    }

    List<Class<?>> classList = new ArrayList<>();
    for (Object obj : array) {
        if (obj != null) {
            classList.add(obj.getClass());
        }
    }

    Class<?>[] classes = new Class(classList.size());
    for (int i = 0; i < classList.size(); i++) {
        classes[i] = classList.get(i);
    }

    return classes;
}