public double chiSquare(double[] expected, long[] observed) throws IllegalArgumentException {
    if (expected.length < 2 || expected.length != observed.length) {
        throw new IllegalArgumentException("observed and expected arrays must be of the same length and not less than 2");
    }
    if (!isPositive(expected) || !isNonNegative(observed)) {
        throw new IllegalArgumentException("observed counts must be non-negative and expected counts must be positive");
    }
    
    double sumSq = 0.0;
    for (int i = 0; i < observed.length; i++) {
        double dev = observed[i] - expected[i];
        sumSq += (dev * dev) / expected[i]; // Calculate chi-square statistic
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
