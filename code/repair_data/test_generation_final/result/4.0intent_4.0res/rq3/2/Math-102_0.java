public double chiSquare(double[] expected, long[] observed) throws IllegalArgumentException {
    if ((expected.length < 2) || (expected.length != observed.length)) {
        throw new IllegalArgumentException("observed and expected array lengths must be at least 2 and equal");
    }
    if (!isPositive(expected) || !isNonNegative(observed)) {
        throw new IllegalArgumentException(
            "expected counts must be positive and observed counts must be non-negative");
    }
    double sumSq = 0.0;
    double dev;
    for (int i = 0; i < observed.length; i++) {
        dev = ((double) observed[i] - expected[i]);
        sumSq += (dev * dev) / expected[i];
    }
    return sumSq;
}

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