public static boolean areEqual(Object o1, Object o2) {
    if (o1 == null || o2 == null) {
        return o1 == null && o2 == null;
    } else if (isArray(o1)) {
        return isArray(o2) && areArraysEqual(o1, o2);
    } else {
        try {
            return o1.equals(o2);
        } catch (RuntimeException e) {
            System.err.println("Exception occurred during equals comparison: " + e.getMessage());
            return false;
        }
    }
}
