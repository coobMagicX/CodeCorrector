public static boolean arrayEquals(double[] a, double[] b) {
    if (a == b) {
        return true; // same reference
    }
    if (a == null || b == null || a.length != b.length) {
        return false; // null or different lengths
    }
    for (int i = 0; i <