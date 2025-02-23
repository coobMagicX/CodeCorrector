public static boolean arrayEquals(double[] a, double[] b) {
    if (a == b) {
        return true; // same reference
    }
    if (a == null || b == null || a.length != b.length) {
        return false; // check for null or different lengths
    }
    for (int i = 0; i < a.length;