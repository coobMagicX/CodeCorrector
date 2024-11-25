public static boolean areEqual(Object o1, Object o2) {
    // Check for nulls first
    if (o1 == null || o2 == null) {
        return o1 == null && o2 == null;
    }
    
    // Check if both objects are arrays and compare them using the array comparison methods
    if (isArray(o1) && isArray(o2)) {
        return areArraysEqual(o1, o2);
    }
    
    // If neither is an array nor are they null, use the equals method of one of the objects
    // This will handle cases where either or both are not arrays and has overridden equals correctly
    return o1.equals(o2) || o2.equals(o1); // We check both ways to ensure symmetry if equals is overridden
}