public static boolean areEqual(Object o1, Object o2) {
    if (o1 == o2) {
        return true;
    }
    if (o1 == null || o2 == null) {
        return false;
    }
    if (isArray(o1) && isArray(o2)) {
        return areArraysEqual(o1, o2);
    }
    try {
        return o1.equals(o2);
    } catch (RuntimeException e) {
        // Log exception or handle it as necessary.
        return false;
    }
}

private static boolean isArray(Object obj) {
    return