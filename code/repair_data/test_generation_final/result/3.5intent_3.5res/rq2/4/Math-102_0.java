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
    double dev = 0.0d;
    for (int i = 0; i < observed.length; i++) {
        dev = ((double) observed[i] - expected[i]);
        sumSq += dev * dev / expected[i];
    }
    return sumSq;
}

public void testChiSquare() {
    double[] expected = {10.0, 15.0, 20.0};
    long[] observed = {8, 17, 22};

    double chiSquareStat = chiSquare(expected, observed);
    double pValue = chiSquareTest(chiSquareStat, expected.length - 1);

    double expectedChiSquareStat = 2.2857142857142856;
    double expectedPValue = 0.3183098861837907;

    // Use a delta value for comparison to avoid precision errors
    double delta = 0.000000000000001;

    assertEquals(expectedChiSquareStat, chiSquareStat, delta);
    assertEquals(expectedPValue, pValue, delta);
}