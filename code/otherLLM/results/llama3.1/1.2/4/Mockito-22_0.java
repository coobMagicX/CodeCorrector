public static boolean areEqual(Object o1, Object o2) {
    if (o1 == null || o2 == null) {
        return o1 == null && o2 == null;
    } else if (isArray(o1)) { // Validate array type equivalence
        if (!isArray(o2) || getArrayComponentType(o1) != getArrayComponentType(o2)) {
            return false;  // Arrays have different component types, not equal
        }
        return areArraysEqual(o1, o2);
    } else {
        try {  // Handle custom equals methods
            return o1.equals(o2);
        } catch (Exception e) {
            return false;
        }
    }
}