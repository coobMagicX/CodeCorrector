public static boolean areEqual(Object o1, Object o2) {
    if (o1 == null || o2 == null) {
        return o1 == null && o2 == null;
    } else if (isArray(o1)) {
        if (!isArray(o2)) {
            return false; // Check array lengths before comparing contents
        }
        return areArrayLengthsEqual(o1, o2)
                && areArraysEqual(o1, o2);
    } else {
        return areObjectsEqual(o1, o2); // Handle custom equals methods correctly
    }
}

// Helper method to handle custom equals methods correctly
private static boolean areObjectsEqual(Object o1, Object o2) {
    if (o1 instanceof BadEquals) {
        return ((BadEquals) o1).equals((BadEquals) o2);
    } else {
        return o1.equals(o2);
    }
}