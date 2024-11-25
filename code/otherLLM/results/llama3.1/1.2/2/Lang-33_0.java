public static Class<?>[] toClass(Object[] array) {
    if (array == null || array.length == 0) {
        return ArrayUtils.EMPTY_CLASS_ARRAY;
    }
    
    // Initialize the Class array with primitive type classes for numeric values
    int nonNullCount = 0;
    boolean hasPrimitiveType = false;
    
    for (Object obj : array) {
        if (obj != null) {
            if (obj instanceof Integer || obj instanceof Double || obj instanceof Float || 
                obj instanceof Long || obj instanceof Short || obj instanceof Byte) {
                hasPrimitiveType = true;
            } else {
                nonNullCount++;
            }
        }
    }

    Class<?>[] classes = new Class[nonNullCount + (hasPrimitiveType ? 1 : 0)];
    
    int j = 0;
    for (Object obj : array) {
        if (obj != null) {
            classes[j++] = obj.getClass();
        }
    }
    
    // If the input array contains primitive types, set the class at index 0 to the corresponding wrapper class
    if (hasPrimitiveType) {
        Class<?>[] temp = new Class[classes.length + 1];
        System.arraycopy(classes, 0, temp, 0, classes.length);
        temp[nonNullCount] = Integer.class; // Assuming int as primitive type, adjust accordingly for other types
        return temp;
    }
    
    return classes;
}