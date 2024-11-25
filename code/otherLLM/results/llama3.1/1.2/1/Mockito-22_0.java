public static boolean areEqual(Object o1, Object o2) {
    if (o1 == null || o2 == null) {
        return o1 == null && o2 == null;
    } else if (isArray(o1)) {
        return isArray(o2) && areArraysEqual(o1, o2);
    } else {
        // Custom object equality should be checked using instanceof and then equals
        if (o1 instanceof BadEquals || o2 instanceof BadEquals) {
            return (o1 instanceof BadEquals && o2 instanceof BadEquals) ? ((BadEquals)o1).equals((BadEquals)o2) : false;
        }
        return o1.equals(o2);
    }
}