public static boolean areEqual(Object o1, Object o2) {
    // Check if either or both objects are null
    if (o1 == null || o2 == null) {
        return o1 == null && o2 == null;
    } else if (isArray(o1) && isArray(o2)) {
        // If both are arrays, compare them as arrays
        return areArraysEqual(o1, o2);
    } else {
        // Otherwise, use the equals method to compare them
        return o1.equals(o2);
    }
}

private static boolean isArray(Object obj) {
    return obj != null && obj.getClass().isArray();
}

private static boolean areArraysEqual(Object array1, Object array2) {
    if (array1 == array2) {
        return true;
    }
    if (array1 == null || array2 == null) {
        return false;
    }
    if (