public static <T> T[] addAll(T[] array1, T... array2) {
    if (array1 == null) {
        return clone(array2);
    } else if (array2 == null) {
        return clone(array1);
    }
    
    Class<?> type1 = array1.getClass().getComponentType();
    Class<?> type2 = array2.getClass().getComponentType();
    
    if (!type1.isAssignableFrom(type2) && !type2.isAssignableFrom(type1)) {
        throw new ArrayStoreException("Incompatible types: " + type1 + " cannot be stored in an array of " + type2);
    }
    
    Class<?> commonType = type1.isAssignableFrom(type2) ? type1 : type2;
    T[] joinedArray = (T[]) Array.newInstance(commonType, array1.length + array2.length);
    
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
    
    return joinedArray;
}
