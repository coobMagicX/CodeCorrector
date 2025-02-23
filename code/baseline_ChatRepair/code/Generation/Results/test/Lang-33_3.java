public static Class<?>[] toClass(Object[] array) {
    if (array == null) {
        return null; 
    }
    
    if (array.length == 0) {
        return ArrayUtils.EMPTY_CLASS_ARRAY; 