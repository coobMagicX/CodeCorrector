public double cumulativeProbability(double x) throws MathException {
    double standardDeviation = getStandardDeviation();
    if (standardDeviation == 0) {
        throw new IllegalArgumentException("Standard deviation cannot be zero.");
    }

    // Avoid division by zero by checking for a zero standard deviation
    if (Double.isInfinite(standardDeviation)) {
        throw new ArithmeticException("Standard deviation is infinite, which is not allowed.");
    }

    double mean = getMean();
    return 0.5 * (1.0 + Erf.erf((x - mean) / (standardDeviation * Math.sqrt(2.0))));
}