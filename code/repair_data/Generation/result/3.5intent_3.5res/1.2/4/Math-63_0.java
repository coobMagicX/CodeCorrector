public static boolean equals(double x, double y) {
    return (Double.isNaN(x) && Double.isNaN(y)) || x == y;
}

public static boolean equals(double[] x, double[] y) {
    if (x == null || y == null) return x == y;
    if (x.length != y.length) return false;
    for (int i = 0; i < x.length; i++) {
        if (!(Double.isNaN(x[i]) && Double.isNaN(y[i])) || x[i] != y[i]) {
            return false;
        }
    }
    return true;
}