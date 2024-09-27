public double cumulativeProbability(double x) throws MathException {
    double z = (x - mean) / (standardDeviation * Math.sqrt(2.0));
    double result = 0.5 * (1.0 + Erf.erf(z));
    return Math.min(1.0, Math.max(0.0, result));
}