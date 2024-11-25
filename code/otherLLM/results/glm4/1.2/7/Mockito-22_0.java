public static boolean areEqual(Object o1, Object o2) {
    if (o1 == null || o2 == null) {
        return o1 == null && o2 == null;
    } else if (isArray(o1)) {
        if (!isArray(o2)) {
            return false; // Cannot compare an array with a non-array object
        }
        return areArraysEqual(o1, o2);
    } else {
        if (isArray(o2)) {
            return false; // Cannot compare a non-array object with an array
        }
        return o1.equals(o2);
    }
}

// Helper methods for arrays
static boolean isArray(Object o) {
    return o.getClass().isArray();
}

static boolean areArrayLengthsEqual(Object o1, Object o2) {
    if (o1 instanceof int[]) {
        return o2 instanceof int[] && ((int[]) o1).length == ((int[]) o2).length;
    } else if (o1 instanceof double[]) {
        return o2 instanceof double[] && ((double[]) o1).length == ((double[]) o2).length;
    } else if (o1 instanceof Object[]) {
        return o2 instanceof Object[] && ((Object[]) o1).length == ((Object[]) o2).length;
    }
    // Add additional checks for other types of arrays if necessary
    return false; // Default case, no type match found
}

static boolean areArrayElementsEqual(Object o1, Object o2) {
    int length = ((Number[]) o1).length;
    for (int i = 0; i < length; i++) {
        if (!areElementTypesCompatible(o1, o2, i)) {
            return false; // Element types are not compatible
        }
    }
    // Assuming that `o1` and `o2` are arrays of Objects for now.
    // If other array element types need to be handled, add the appropriate checks here.
    return true;
}

static boolean areElementTypesCompatible(Object o1, Object o2, int index) {
    if (o1 instanceof Integer && o2 instanceof Integer) {
        return true; // Integer elements are compatible
    } else if (o1 instanceof Double && o2 instanceof Double) {
        return true; // Double elements are compatible
    } else if (o1 instanceof Object && o2 instanceof Object) {
        return true; // Generic object references are compatible
    }
    // Add additional checks for other types if necessary
    return false; // Default case, no type match found
}