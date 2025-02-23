public double chiSquare(double[] expected, long[] observed) throws IllegalArgumentException {
    if (expected.length < 2 || expected.length != observed.length) {
        throw new IllegalArgumentException("observed and expected arrays must be of the same length and not less than 2");
    }
    if (!isPositive(expected) || !isNonNegative(observed)) {
        throw new IllegalArgumentException("observed counts must be non-negative and expected counts must be positive");
    }
    
    double sumSq = 0.0;
    for (int i = 0; i < observed.length; i++) {
        double deviation = (double) observed[i] - expected[i];
        sumSq += (deviation * deviation) / expected[i];
    }
    return sumSq;
}
