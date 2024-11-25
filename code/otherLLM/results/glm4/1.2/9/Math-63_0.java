public static boolean equals(double[] x, double[] y) {
    if ((x == null) || (y == null)) {
        return !((x == null) ^ (y == null));
    }
    if (x.length != y.length) {
        return false;
    }
    for (int i = 0; i < x.length; ++i) {
        double xi = x[i];
        double yi = y[i];
        // Check both NaN conditions and equality of elements
        if (!((Double.isNaN(xi) && Double.isNaN(yi)) || xi == yi)) {
            return false;
        }
    }
    return true;
}