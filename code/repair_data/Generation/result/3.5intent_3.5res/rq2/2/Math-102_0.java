public double chiSquare(double[] expected, long[] observed)
        throws IllegalArgumentException {
    if ((expected.length < 2) || (expected.length != observed.length)) {
        throw new IllegalArgumentException(
                "observed, expected array lengths incorrect");
    }
    if (!isPositive(expected) || !isNonNegative(observed)) {
        throw new IllegalArgumentException(
            "observed counts must be non-negative and expected counts must be postive");
    }
    double sumSq = 0.0d;
    for (int i = 0; i < observed.length; i++) {
        sumSq += (((double) observed[i] - expected[i]) * 
                ((double) observed[i] - expected[i])) / expected[i];
    }
    return sumSq;
}

private boolean isPositive(double[] values) {
    for (double value : values) {
        if (value <= 0.0d) {
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