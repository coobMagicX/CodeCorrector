public static boolean equals(double[] x, double[] y) {
    if (x == null || y == null) return false;
    if (x.length != y.length) return false;

    for (int i = 0; i < x.length; i++) {
        if (Double.isNaN(x[i]) && Double.isNaN(y[i])) continue;
        if (Double.isInfinite(x[i]) != Double.isInfinite(y[i]) || 
            (Double.isInfinite(x[i]) && MathUtils.log(Math.abs(x[i]), Math.abs(y[i])) != 0)) return false;
        if (!equals(x[i], y[i])) return false;
    }

    return true;
}