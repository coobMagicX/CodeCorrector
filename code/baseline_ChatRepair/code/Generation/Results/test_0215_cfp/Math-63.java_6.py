public static boolean equals(double[] a, double[] b) {
    if (a == b) {
        return true; // same reference
    }
    if (a == null || b == null || a.length != b