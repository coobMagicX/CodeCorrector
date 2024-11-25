public static boolean equals(double[] x, double[] y) {
    // Handle null input and array length mismatch
    if (x == null && y == null) {
        return true; // Both arrays are null, consider them equal
    }
    if (x == null || y == null || x.length != y.length) {
        return false; // One array is null or they have different lengths
    }

    // Check for NaN and infinity values in both arrays
    for (int i = 0; i < x.length; i++) {
        if (Double.isNaN(x[i]) && Double.isNaN(y[i])) {
            continue; // Both elements are NaN, consider them equal
        }
        if (Double.isInfinite(x[i]) || Double.isInfinite(y[i])) {
            return false; // One element is infinite and the other is not
        }
    }

    // Perform element-wise comparison of the input arrays
    for (int i = 0; i < x.length; i++) {
        if (!equals(x[i], y[i])) { // Use a helper method to compare individual elements
            return false;
        }
    }

    return true; // All elements are equal
}

// Helper method to compare individual double values
public static boolean equals(double x, double y) {
    return (Double.isNaN(x) && Double.isNaN(y)) || x == y;
}