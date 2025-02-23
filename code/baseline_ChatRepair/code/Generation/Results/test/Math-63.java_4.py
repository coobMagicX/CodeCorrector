public static boolean equals(double[] x, double[] y) {
    if (x == y) {
        return true;
    }
    if (x == null || y == null || x.length != y.length) {
        return false;
    }
    for (int i = 0; i < x.length; i++) {
        if (Double.is