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
    double expectedValue = 0.0d;
    for (int i = 0; i < observed.length; i++) {
        expectedValue = (expected[i] * getTotal(observed)) / getTotal(expected);
        sumSq += ((observed[i] - expectedValue) * (observed[i] - expectedValue)) / expectedValue;
    }
    return sumSq;
}

private double getTotal(long[] arr) {
    double total = 0.0d;
    for (long value : arr) {
        total += value;
    }
    return total;
}

private double getTotal(double[] arr) {
    double total = 0.0d;
    for (double value : arr) {
        total += value;
    }
    return total;
}

private boolean isPositive(double[] arr) {
    for (double value : arr) {
        if (value <= 0) {
            return false;
        }
    }
    return true;
}

private boolean isNonNegative(long[] arr) {
    for (long value : arr) {
        if (value < 0) {
            return false;
        }
    }
    return true;
}