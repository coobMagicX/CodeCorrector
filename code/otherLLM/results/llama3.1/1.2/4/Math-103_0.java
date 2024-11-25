public double cumulativeProbability(double x) throws MathException {
    if (x < getMean() - getStandardDeviation() * Math.sqrt(2.0)) {
        return 0.5; // lower tail probability for small negative values
    } else if (x > getMean() + getStandardDeviation() * Math.sqrt(2.0)) {
        return 1.0 - 0.5; // upper tail probability for large positive values
    }
    
    double normalizedX = (x - getMean()) / (getStandardDeviation() * Math.sqrt(2.0));
    return 0.5 * (1.0 + Erf.erf(normalizedX));
}