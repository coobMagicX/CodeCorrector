public double chiSquare(double[] expected, long[] observed)
throws IllegalArgumentException {
    if ((expected.length < 2) || (expected.length != observed.length)) {
        throw new IllegalArgumentException(
                "observed, expected array lengths incorrect");
    }
    if (!isPositive(expected) || !isNonNegative(observed)) {
        throw new IllegalArgumentException(
            "observed counts must be non-negative and expected counts must be positive");
    }
    double sumSq = 0.0d;
    for (int i = 0; i < observed.length; i++) {
        double expectedValue = expected[i];
        double dev = ((double) observed[i] - expectedValue);
        // Adjusting the calculation to match the chiSquare method
        // by dividing by 'expectedValue' instead of '1.0d'
        sumSq += dev * dev / expectedValue;
    }
    return sumSq;
}

// Helper methods that should be defined elsewhere in the class or imported if available:

private boolean isPositive(double[] array) {
    for (double value : array) {
        if (value <= 0.0d) {
            return false;
        }
    }
    return true;
}

private boolean isNonNegative(long[] array) {
    for (long value : array) {
        if (value < 0) {
            return false;
        }
    }
    return true;
}