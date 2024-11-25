public static boolean equals(double[] x, double[] y) {
    if ((x == null) || (y == null)) {
        return !((x == null) ^ (y == null));
    }
    if (x.length != y.length) {
        return false;
    }
    for (int i = 0; i < x.length; ++i) {
        double dx = x[i];
        double dy = y[i];
        boolean isBothNaN = Double.isNaN(dx) && Double.isNaN(dy);
        if (!isBothNaN && (dx != dy)) {
            // Use the existing methods to handle NaNs and special cases
            double diff = Math.abs(dx - dy);
            double tolerance = 1E-9; // This is a small value that can be used as an acceptable difference
            boolean isCloseEnough = diff < tolerance;
            if (!isBothNaN && !isCloseEnough) {
                return false;
            }
        }
    }
    return true;
}