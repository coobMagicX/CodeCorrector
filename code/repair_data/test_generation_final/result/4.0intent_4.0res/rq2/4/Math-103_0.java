public double cumulativeProbability(double x) throws MathException {
    double z = (x - mean) / (standardDeviation * Math.sqrt(2.0));
    return 0.5 * (1.0 + Erf.erf(z));
}