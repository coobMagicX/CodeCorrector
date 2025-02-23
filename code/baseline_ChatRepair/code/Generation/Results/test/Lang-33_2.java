public static Class<?>[] toClass(Object[] array) {
    if (array == null) {
        return null;
    } else if (array.length == 0) {
        return ArrayUtils.EMPTY_CLASS_ARRAY;
    }
    
    return Arrays.stream(array)
                 .map(obj -> obj != null ? obj.getClass() : null)
                 .toArray(Class<?>[]::new);
}
