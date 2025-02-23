public double chiSquare(double[] expected, long[] observed) throws IllegalArgumentException {
    // Check if the arrays are correctly sized and there is enough data to compute chi-square.
    if ((expected.length < 2) || (expected.length != observed.length)) {
        throw new IllegalArgumentException("observed and expected arrays must be the same length and at least of length 2");
    }
    // Validate that the data within the arrays meets the requirements
    if (!isPositive(expected) || !isNonNegative(observed)) {
        throw new IllegalArgumentException("Expected values must be positive and observed values must be non-negative");
    }

    double sumSq = 0.0;  // This will hold the chi-square statistic
    for (int i = 0; i < observed.length; i++) {
        // Calculate the deviation from expected values
        double deviation = observed[i] - expected[i];
        // Compute the chi-square component for the current element
        double chiSquareComponent = (deviation * deviation) / expected[i];
        // Accumulate to the running total
        sumSq += chiSquareComponent;
    }
    // Return the computed chi-square statistic
    return sumSq;
}
