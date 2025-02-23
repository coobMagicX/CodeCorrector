public static boolean areEqual(Object o1, Object o2) {
    // Check if both objects refer to the same instance
    if (o1 == o2) {
        return true;
    }

    // Check if either object is null
    if (o1 == null || o2 == null) {
        return false;
    }

    // If both are arrays, compare as arrays
    if (isArray(o1) && isArray(o2)) {
        return areArraysEqual(o1, o2);
    }

    // Fallback to using equals method and handling cases where it might be improperly implemented
    try {
        return o1.equals(o2);
    } catch (RuntimeException e) {
        System.err