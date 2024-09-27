public double cumulativeProbability(double x) throws MathException {
    setMean(0); // Set the mean to 0
    setStandardDeviation(1); // Set the standard deviation to 1
    return 0.5 * (1.0 + Erf.erf((x - mean) / (standardDeviation * Math.sqrt(2.0))));
}