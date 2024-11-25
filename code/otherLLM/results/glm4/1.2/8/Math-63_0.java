public static boolean equals(double[] x, double[] y) {
    if ((x == null) || (y == null)) {
        return !((x == null) ^ (y == null));
    }
    if (x.length != y.length) {
        return false;
    }
    for (int i = 0; i < x.length; ++i) {
        if (!equals(x[i], y[i])) {
            return false;
        }
    }
    return true;
}

public static boolean equals(double x, double y) {
    if (Double.isNaN(x) && Double.isNaN(y)) {
        // Both numbers are NaN
        return true;
    } else if (Double.isInfinite(x) || Double.isInfinite(y)) {
        // If either is infinite, they are not equal unless both are positive or both are negative
        return Double.compare(x, y) == 0;
    } else {
        // Normal comparison for finite numbers
        return Double.compare(x, y) == 0;
    }
}