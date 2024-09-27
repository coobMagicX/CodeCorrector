public static boolean equals(double[] x, double[] y) {
    if (x == null || y == null) return x == y;
    if (x.length != y.length) return false;
    for (int i = 0; i < x.length; i++) {
        if (!Double.isNaN(x[i]) && !Double.isNaN(y[i])) {
            if (x[i] != y[i]) {
                return false;
            }
        } else if (!(Double.isNaN(x[i]) && Double.isNaN(y[i]))) {
            return false;
        }
    }
    return true;
}