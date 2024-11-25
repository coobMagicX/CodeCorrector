public static boolean areEqual(Object o1, Object o2) {
    if (o1 == null || o2 == null) {
        return o1 == null && o2 == null;
    } else if (isArray(o1)) {
        if (isArray(o2)) { // Add type-specific handling for array types
            return areArraysEqual(o1, o2);
        } else if (o2 instanceof PrimitiveArray<?>){ // Add primitive array equivalence check
            return false; // Arrays of different lengths or incompatible types are not equal
        }
    } else if (o1 instanceof BadEquals) { // Fix BadEquals handling by checking instance type
        return ((BadEquals)o1).equals((BadEquals)o2);
    } else {
        try {
            return o1.equals(o2);
        } catch (ClassCastException e) {} // Ignore ClassCastException for custom objects with overridden equals method
    }
    return false;
}

// Added method to check if an object is a primitive array
static boolean isArray(Object obj) {
    return obj instanceof int[] || obj instanceof float[] || obj instanceof double[] || obj instanceof long[] || obj instanceof short[] || obj instanceof char[];
}