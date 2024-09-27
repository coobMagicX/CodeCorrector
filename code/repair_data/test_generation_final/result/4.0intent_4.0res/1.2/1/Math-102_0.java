public double chiSquare(double[] expected, long[] observed) throws IllegalArgumentException {
    if ((expected.length < 2) || (expected.length != observed.length)) {
        throw new IllegalArgumentException(
                "observed, expected array lengths incorrect");
    }
    if (!isPositive(expected) || !isNonNegative(observed)) {
        throw new IllegalArgumentException(
            "observed counts must be non-negative and expected counts must be positive");
    }
    double sumSq = 0.0d;
    double dev = 0.0d;
    for (int i = 0; i < observed.length; i++) {
        // Use double casting to ensure precision in division and multiplication
        double observedDouble = (double) observed[i];
        // Calculate the deviation and then the component of the chi-square sum
        dev = observedDouble - expected[i];
        sumSq += (dev * dev) / expected[i];  // Corrected formula part
    }
    return sumSq;
}

// Supporting methods assumed to exist based on previous code context
private boolean isPositive(double[] array) {
    for (double value : array) {
        if (value <= 0) {
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