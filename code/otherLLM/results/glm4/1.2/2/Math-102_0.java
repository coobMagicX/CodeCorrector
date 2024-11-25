public double chiSquare(double[] expected, long[] observed)
throws IllegalArgumentException {
    if (expected.length != observed.length) {
        throw new IllegalArgumentException(
                "observed and expected array lengths are not equal");
    }
    if (!isPositive(expected)) {
        throw new IllegalArgumentException(
            "all elements in the expected array must be positive");
    }
    if (isNegative(observed)) {
        throw new IllegalArgumentException(
            "observed counts cannot contain negative values");
    }

    double sumSq = 0.0d;
    double dev = 0.0d;
    for (int i = 0; i < observed.length; i++) {
        dev = ((double) observed[i] - expected[i]);
        // Add a check to prevent division by zero
        if (expected[i] == 0) {
            throw new ArithmeticException("Expected value cannot be zero");
        }
        sumSq += dev * dev / expected[i];
    }

    return sumSq;
}

private boolean isPositive(double[] array) {
    for (double num : array) {
        if (num <= 0) {
            return false;
        }
    }
    return true;
}

private boolean isNonNegative(long[] array) {
    for (long num : array) {
        if (num < 0) {
            return false;
        }
    }
    return true;
}

private boolean isNegative(long[] array) {
    for (long num : array) {
        if (num < 0) {
            return true;
        }
    }
    return false;
}