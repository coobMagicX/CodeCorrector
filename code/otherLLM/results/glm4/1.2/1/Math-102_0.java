public double chiSquare(double[] expected, long[] observed)
        throws IllegalArgumentException {
    if ((expected.length < 2) || (expected.length != observed.length)) {
        throw new IllegalArgumentException(
                "observed, expected array lengths incorrect");
    }
    if (!isPositive(expected)) {
        throw new IllegalArgumentException(
            "expected counts must be positive");
    }
    double sumSq = 0.0d;
    double dev = 0.0d;
    for (int i = 0; i < observed.length; i++) {
        dev = ((double) observed[i] - expected[i]);
        // Correcting to divide by the expected value of type double
        sumSq += dev * dev / expected[i];
    }
    return sumSq;
}