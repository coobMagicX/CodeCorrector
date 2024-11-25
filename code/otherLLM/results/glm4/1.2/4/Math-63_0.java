public static boolean equals(double[] x, double[] y) {
    // Check for null or different lengths first.
    if ((x == null) || (y == null)) {
        return !((x == null) ^ (y == null));
    }
    if (x.length != y.length) {
        return false;
    }

    // Use the existing equals method to compare each pair of elements.
    for (int i = 0; i < x.length; ++i) {
        if (!equals(x[i], y[i])) {
            return false;
        }
    }
    return true;
}

// Modify the equals method to handle comparison of double values correctly.
public static boolean equals(double a, double b) {
    // Check for NaN or infinity cases first.
    if (Double.isNaN(a) || Double.isNaN(b)) {
        return Double.compare(a, b) == 0; // NaN is equal to itself
    }
    if (a == b) {
        return true;
    }
    // Special case for positive and negative zero comparison.
    if ((a != 0 && -a == b) || (b != 0 && a == -b)) {
        return false;
    }

    // Use Double.compare to get the correct result in all other cases.
    return Double.compare(a, b) == 0;
}