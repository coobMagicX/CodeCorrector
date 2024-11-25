public static boolean areEqual(Object o1, Object o2) {
    if (o1 == null || o2 == null) {
        return o1 == null && o2 == null;
    } else if (isArray(o1)) {
        if (!isArray(o2)) {
            // If one is an array and the other isn't, they can't be equal.
            return false;
        }
        return areArraysEqual(o1, o2);
    } else {
        if (o1 instanceof BadEquals && o2 instanceof BadEquals) {
            // If both objects are instances of BadEquals, directly call their equals() method
            return ((BadEquals) o1).equals(o2);
        } else {
            // For non-array or different types of BadEquals instances, use the existing equals check
            return o1.equals(o2);
        }
    }
}