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
        double dev = ((double) observed[i] - expected[i]);
        // Use the chiSquare method's calculation pattern to handle expected values
        if (expected[i] > 0) { // To avoid division by zero
            sumSq += dev * dev / expected[i];
        } else {
            throw new IllegalArgumentException(
                "expected value at index " + i + " is non-positive");
        }
    }
    return sumSq;
}

// Assuming the following methods exist as per the context:
private boolean isPositive(double[] array) {
    for (double d : array) {
        if (d <= 0) {
            return false;
        }
    }
    return true;
}

private boolean isNonNegative(long[] array) {
    for (long l : array) {
        if (l < 0) {
            return false;
        }
    }
    return true;
}