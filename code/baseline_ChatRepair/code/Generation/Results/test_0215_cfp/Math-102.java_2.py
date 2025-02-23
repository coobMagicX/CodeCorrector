public double chiSquare(double[] expected, long[] observed) throws IllegalArgumentException {
    // Input validation
    if (expected.length < 2 || expected.length != observed.length) {
        throw new IllegalArgumentException("Arrays must be of the same length and at least 2 elements long.");
    }

    // Confirm that observed values are non-negative, expected values are positive
    for (double value : expected) {
        if (value <= 0) {
            throw new IllegalArgumentException("Expected counts must be positive.");
        }
    }
    for (long value : observed) {
        if (value < 0) {
            throw new IllegalArgumentException("Observed counts must be non-negative.");
        }
    }

    // Calculate chi-square statistic
    double sumSq = 0.0;
    for (int i = 0; i < observed.length; i++) {
        if (expected[i] < 5) {
            throw new IllegalArgumentException("All expected counts must be at least 5 for valid chi-square approximation.");
        }
        double difference = observed[i] - expected[i];
        double squareOfDifference = difference * difference;
        sumSq += squareOfDifference / expected[i];
    }

    return sumSq;
}
