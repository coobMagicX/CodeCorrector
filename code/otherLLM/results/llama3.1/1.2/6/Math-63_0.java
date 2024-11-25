public static boolean equals(double[] x, double[] y) {
    if (x == null && y == null) return true; // Handle null array inputs
    if (x == null || y == null) return false; // Handle null array inputs
    if (x.length != y.length) return false; // Handle empty array comparisons

    for (int i = 0; i < x.length; i++) {
        if (!Double.isNaN(x[i]) && !Double.isNaN(y[i])) { // Correctly compare NaN values
            if (x[i] == y[i]) continue;
            else return false;
        } else if (Double.isNaN(x[i]) != Double.isNaN(y[i])) { // Correctly compare NaN values
            return false;
        }
    }

    return true; // Handle empty array comparisons
}