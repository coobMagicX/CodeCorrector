public static boolean areEqual(Object o1, Object o2) {
    if (o1 == null || o2 == null) {
        return o1 == null && o2 == null;
    } else if (isArray(o1) && isArray(o2)) {
        return areArraysEqual(o1, o2);
    } else {
        return o1.equals(o2);
    }
}

private static boolean