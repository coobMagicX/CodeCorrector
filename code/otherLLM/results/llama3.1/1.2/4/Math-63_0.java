public static boolean equals(double x, double y) {
    // 1. Handle null array inputs by considering them as separate objects
    if (Double.isNaN(x)) return Double.isNaN(y);
    if (Double.isNaN(y)) return Double.isNaN(x);
    
    // 2. Compare arrays with different lengths or NaN/infinity values
    if ((x != x) && (y != y)) {
        return true; // both are NaN
    }
    if (x == Double.POSITIVE_INFINITY || x == Double.NEGATIVE_INFINITY ||
            y == Double.POSITIVE_INFINITY || y == Double.NEGATIVE_INFINITY) {
        return x == y;
    }
    
    // 3. Compare arrays with contents, not references
    double[] xArray = {x};
    double[] yArray = {y};
    return equals(xArray, yArray);
}