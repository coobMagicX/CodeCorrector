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
    for (int i = 0; i < a.length; i++) {
        if (Double.isNaN(a[i]) && Double.isNaN(b[i])) {
            continue;
        }
        if (a[i] != b[i]) {
            return false;
        }
    }
    return true;
}