public static boolean equals(double[] x, double[] y) {
    if ((x == null) || (y == null)) {
        // If both arrays are null, they are considered equal.
        // If only one of them is null, the other is not, so they are not equal.
        return !((x == null) ^ (y == null));
    }
    if (x.length != y.length) {
        // Arrays with different lengths cannot be equal.
        return false;
    }
    for (int i = 0; i < x.length; ++i) {
        if (!equals(x[i], y[i])) {
            // If any pair of elements are not equal, the arrays are not equal.
            return false;
        }
    }
    return true;
}

public static boolean equals(double x, double y) {
    // Check for NaN values separately to avoid comparing them with Double.NaN
    if (Double.isNaN(x)) {
        return Double.isNaN(y);
    }
    if (Double.isNaN(y)) {
        return false; // Different from x being NaN
    }
    // Use a tolerance value to compare floating-point numbers.
    // The tolerance is necessary due to the imprecision of floating-point arithmetic.
    double tolerance = 1e-9;
    return Math.abs(x - y) < tolerance;
}