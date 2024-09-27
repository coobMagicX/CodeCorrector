public static boolean areEqual(Object o1, Object o2) {
    if (o1 == null || o2 == null) {
        return o1 == null && o2 == null;
    } else if (o1.getClass().isArray()) {
        return o2.getClass().isArray() && areArraysEqual(o1, o2);
    } else {
        return o1.equals(o2);
    }
}