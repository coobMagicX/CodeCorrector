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
        dev = ((double) observed[i] - expected[i]);
        sumSq += dev * dev / expected[i];
    }
    return sumSq;
}

public double chiSquareTest(double[] expected, long[] observed)
        throws IllegalArgumentException {
    double chiSquareStat = chiSquare(expected, observed);
    double pValue = chiSquarePValue(chiSquareStat, expected.length - 1);
    return pValue;
}

public double chiSquarePValue(double chiSquareStat, int degreesOfFreedom) {
    ChiSquaredDistribution chiSquaredDistribution = new ChiSquaredDistribution(degreesOfFreedom);
    double pValue = 1.0d - chiSquaredDistribution.cumulativeProbability(chiSquareStat);
    return pValue;
}

public boolean isPositive(double[] values) {
    for (double value : values) {
        if (value <= 0) {
            return false;
        }
    }
    return true;
}

public boolean isNonNegative(long[] values) {
    for (long value : values) {
        if (value < 0) {
            return false;
        }
    }
    return true;
}