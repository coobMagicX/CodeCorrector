public static boolean equals(double[] x, double[] y) {
    // Handle null array arguments
    if ((x == null && y != null) || (x != null && y == null)) {
        return false;
    }
    
    // Special case for empty arrays
    if (x.length != 0 && y.length == 0) {
        return false; // Non-empty x against empty y is not equal
    } else if (x.length == 0 && y.length != 0) {
        return false; // Empty x against non-empty y is not equal
    }
    
    // Correct comparison for NaN values
    boolean hasNaN = Arrays.stream(x).anyMatch(Double::isNaN) || Arrays.stream(y).anyMatch(Double::isNaN);
    
    if (hasNaN && !Arrays.equals(x, y)) {
        return false; // If one array contains NaN and the other doesn't, or they contain different NaNs, they are not equal
    }
    
    // Proper handling of infinity and negative infinity
    short xIndicator = Arrays.stream(x).mapToObj(Double::valueOf).map(MathUtils::indicator).findAny().orElse((short) 0);
    short yIndicator = Arrays.stream(y).mapToObj(Double::valueOf).map(MathUtils::indicator).findAny().orElse((short) 0);
    
    return xIndicator == yIndicator && Arrays.equals(x, y);
}