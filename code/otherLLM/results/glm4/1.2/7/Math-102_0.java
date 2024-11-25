public double chiSquare(double[] expected, long[] observed)
throws IllegalArgumentException {
    if ((expected.length < 2) || (expected.length != observed.length)) {
        throw new IllegalArgumentException(
                "expected and observed array lengths incorrect");
    }
    if (!isPositive(expected) || !isNonNegative(observed)) {
        throw new IllegalArgumentException(
            "observed counts must be non-negative and expected counts must be positive");
    }
    
    double sumSq = 0.0d;
    for (int i = 0; i < observed.length; i++) {
        if (expected[i] == 0) {
            throw new IllegalArgumentException(
                    "expected count at index " + i + " is zero, cannot divide by zero");
        }
        double dev = ((double) observed[i] - expected[i]);
        sumSq += dev * dev / expected[i];
    }
    
    return sumSq;
}

// Helper methods (assuming they are defined elsewhere)
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