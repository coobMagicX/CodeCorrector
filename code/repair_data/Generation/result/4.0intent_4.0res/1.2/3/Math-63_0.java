public static boolean equals(double[] a, double[] b) {
    if (a == null && b == null) {
        return true;
    }
    if ((a == null && b != null) || (a != null && b == null)) {
        return false;
    }
    if (a.length != b.length) {
        return false;
    }
    for (int i = 0; i < a.length; i++) {
        if ((Double.isNaN(a[i]) && !Double.isNaN(b[i])) || (!Double.isNaN(a[i]) && Double.isNaN(b[i]))) {
            return false;
        }
        if (a[i] != b[i]) {
            return false;
        }
    }
    return true;
}