public static boolean areEqual(Object o1, Object o2) {
    if (o1 == null || o2 == null) {
        return o1 == null && o2 == null;
    }
    
    if (isArray(o1)) {
        if (!isArray(o2)) {
            return false; // Different types cannot be equal if one is an array
        }
        return areArraysEqual(o1, o2);
    } else if (isArray(o2)) {
        return false; // Different types cannot be equal if one is an array and the other isn't
    }

    return o1.equals(o2);
}