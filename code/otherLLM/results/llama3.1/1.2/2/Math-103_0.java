public double cumulativeProbability(double x) throws MathException {
    double z = (x - mean) / getStandardDeviation(); // Utilize existing method for standard deviation
    return 0.5 * (1.0 + Erf.erf(z));
}