public double cumulativeProbability(double x) throws MathException {
    setMean(mean); // Set the mean value
    setStandardDeviation(standardDeviation); // Set the standard deviation value
    return 0.5 * (1.0 + Erf.erf((x - mean) / (standardDeviation * Math.sqrt(2.0))));
}