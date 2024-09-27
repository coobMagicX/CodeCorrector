public static boolean areEqual(Object o1, Object o2) {
    if (o1 == null || o2 == null) {
        return o1 == null && o2 == null;
    } else if (isArray(o1)) {
        return isArray(o2) && areArraysEqual(o1, o2);
    } else if (o1.getClass() != o2.getClass()) {
        return false;
    } else {
        try {
            return o1.equals(o2);
        } catch (Exception e) {
            return false;
        }
    }
}