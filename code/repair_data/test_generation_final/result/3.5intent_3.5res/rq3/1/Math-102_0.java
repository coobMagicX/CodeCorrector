public double chiSquare(double[] expected, long[] observed) throws IllegalArgumentException {
    if (expected.length < 2 || expected.length != observed.length) {
        throw new IllegalArgumentException("Observed and expected array lengths incorrect");
    }
    if (!isNonNegative(expected) || !isNonNegative(observed)) {
        throw new IllegalArgumentException("Observed counts must be non-negative and expected counts must be non-negative");
    }
    double sumSq = 0.0;
    for (int i = 0; i < observed.length; i++) {
        double dev = observed[i] - expected[i];
        sumSq += dev * dev / expected[i];
    }
    return sumSq;
}

private boolean isNonNegative(double[] values) {
    for (double value : values) {
        if (value < 0) {
            return false;
        }
    }
    return true;
}

private boolean isNonNegative(long[] values) {
    for (long value : values) {
        if (value < 0) {
            return false;
        }
    }
    return true;
}