public static boolean equals(double[] a, double[] b) {
    if (a == b) {
        return true;
    }
    if (a == null || b == null) {
        return false;
    }
    if (a.length != b.length) {
        return false;
    }
    for