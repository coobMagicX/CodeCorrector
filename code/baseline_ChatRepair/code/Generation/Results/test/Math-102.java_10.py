public double chiSquare(double[] expected, long[] observed) throws IllegalArgumentException {
    // Check if arrays are valid for processing
    if (expected.length < 2 || expected.length != observed.length) {
        throw new IllegalArgumentException("observed and expected array lengths are incorrect, or less than 2.");
    }
    // Ensure all expected values are positive and all observed values are non-negative
    if (!isPositive(expected) || !isNonNegative(observed)) {
        throw new IllegalArgumentException("Observed counts must be non-negative and expected counts must be positive");
    }

    double sumSq = 0.0;
    for (int i = 0; i < observed.length; i++) {
        double dev = (double) observed[i] - expected[i];
        sumSq += (dev * dev) / expected[i];
    }
    return sumSq;
}

private boolean isPositive(double[] array) {
    for (double v : array) {
        if (v <= 0) return false;
    }
    return true;
}

private boolean isNonNegative(long[] array) {
    for (long v : array) {
        if (v < 0) return false;
    }
    return true;
}
