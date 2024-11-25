public static boolean areEqual(Object o1, Object o2) {
    if (o1 == null || o2 == null) {
        return o1 == null && o2 == null;
    } else if (isArray(o1)) {
        return isArray(o2)
                && areArraysEqual(o1, o2);
    } else {
        // Add type-specific equality check for primitive types
        if (o1 instanceof Integer && o2 instanceof Integer) {
            return ((Integer) o1).equals((Integer) o2);
        } 
        // Add more type-specific checks as needed
        
        return o1.equals(o2);
    }
}