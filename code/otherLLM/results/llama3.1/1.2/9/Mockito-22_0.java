public static boolean areEqual(Object o1, Object o2) {
    if (o1 == null || o2 == null) {
        return o1 == null && o2 == null;
    } else if (isArray(o1)) {
        return isArray(o2)
                && areArraysEqual(o1, o2);
    } else if (o1 instanceof BadEquals) { // Added custom equals handling
        return ((BadEquals) o1).equals((BadEquals) o2);
    } else {
        return o1.equals(o2);
    }
}