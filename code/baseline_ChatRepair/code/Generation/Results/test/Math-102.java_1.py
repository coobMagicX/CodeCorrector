public double chiSquare(double[] expected, long[] observed) throws IllegalArgumentException {
    if ((expected.length < 2) || (expected.length != observed.length)) {
        throw new IllegalArgumentException("Observed and expected array lengths must be the same and at least 2");
    }
    if (!isPositive(expected) || !isNonNegative(observed)) {
        throw new IllegalArgumentException("Observed counts must be non-negative and expected counts must be positive");
    }

    // Check if expected values are sufficiently large for chi-square approximation to be valid
    for (double e : expected) {
        if (e < 5) {
            throw new IllegalArgumentException("All expected values should be at least 5 for the chi-square approximation to be valid");
        }
    }

    double sumSq = 0.0;
    double dev;
    for (int i = 0; i < observed.length; i++) {
        dev = ((double) observed[i] - expected[i]);
        sumSq += dev * dev / expected[i];
    }
    return sumSq;
}
