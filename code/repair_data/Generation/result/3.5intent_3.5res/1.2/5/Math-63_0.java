public static boolean equals(double[] x, double[] y) {
    if (x == null && y == null) {
        return true;
    }
    if (x == null || y == null) {
        return false;
    }
    if (x.length != y.length) {
        return false;
    }
    for (int i = 0; i < x.length; i++) {
        if (Double.compare(x[i], y[i]) != 0) {
            return false;
        }
    }
    return true;
}