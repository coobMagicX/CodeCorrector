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

    // Use a long for the sum to avoid overflow
    long sumSq = 0;
    double dev = 0.0d;
    double totalExpected = 0.0;

    // Calculate the total of expected values to handle potential large numbers
    for (double val : expected) {
        totalExpected += val;
    }

    // Use a long for intermediate calculations to avoid overflow
    for (int i = 0; i < observed.length; i++) {
        dev = ((double) observed[i] - expected[i]);
        sumSq += (long)(dev * dev / expected[i]);
    }

    // Cast the result back to double before returning to match the expected return type
    return (double)sumSq;
}