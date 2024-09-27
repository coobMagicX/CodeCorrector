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
    double dev = 0.0d;
    for (int i = 0; i < observed.length; i++) {
        if (expected[i] == 0) {
            if (observed[i] == 0) {
                continue; // Skip this term if expected is zero and observed is zero
            } else {
                throw new IllegalArgumentException("Expected count must not be zero if observed is non-zero");
            }
        }
        dev = ((double) observed[i] - expected[i]);
        sumSq += dev * dev / expected[i];
    }
    return sumSq;
}