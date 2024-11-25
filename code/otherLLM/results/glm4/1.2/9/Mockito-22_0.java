public static boolean areEqual(Object o1, Object o2) {
    if (o1 == null || o2 == null) {
        // Corrected to check for both being null
        return o1 == null && o2 == null;
    } else if (isArray(o1)) {
        return isArray(o2) && areArraysEqual(o1, o2);
    } else if (o1.getClass() != o2.getClass()) {
        // Check for class equality before comparing content to avoid calling equals on incompatible types
        return false;
    } else {
        // Directly use the equals method without unnecessary checks now that we have handled nulls and array type comparison
        return o1.equals(o2);
    }
}

// Assuming the methods are defined as follows:
static boolean isArray(Object o) {
    return o.getClass().isArray();
}

static boolean areArrayLengthsEqual(Object o1, Object o2) {
    if (!isArray(o1) || !isArray(o2)) throw new IllegalArgumentException("Objects must be arrays");
    return Arrays.equals(o1, o2);
}

// Note: The method `areArrayElementsEqual` is not provided but assumed to exist and perform the necessary checks.

// Example usage of areArrayLengthsEqual (assuming its implementation):
static boolean areArraysEqual(Object o1, Object o2) {
    if (!isArray(o1) || !isArray(o2)) throw new IllegalArgumentException("Objects must be arrays");
    return areArrayLengthsEqual(o1, o2);
}