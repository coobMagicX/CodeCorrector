public static boolean equals(double[] x, double[] y) {
    if ((x == null) || (y == null)) {
        return !((x == null) ^ (y == null));
    }
    if (x.length != y.length) {
        return false;
    }

    for (int i = 0; i < x.length; ++i) {
        double valX = x[i];
        double valY = y[i];

        // Check for NaN or equality
        if (Double.isNaN(valX) && Double.isNaN(valY)) {
            continue; // Both are NaN, continue to next iteration
        }
        
        if ((valX != valX) && (valY != valY)) { // Check for NaN values using the "not equal to itself" trick
            continue; // Both values are NaN, continue to next iteration
        }

        if (!Double.compare(valX, valY) == 0) {
            return false;
        }
    }
    return true;
}