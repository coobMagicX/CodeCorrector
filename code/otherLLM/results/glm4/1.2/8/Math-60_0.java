public double cumulativeProbability(double x) throws MathException {
    final double dev = x - mean;
    if (Double.isInfinite(dev)) { // Check for infinity directly
        return dev > 0 ? 1 : 0; // Return 1 for positive infinity, 0 for negative infinity
    }
    try {
        return 0.5 * (1.0 + Erf.erf((dev) / (standardDeviation * FastMath.sqrt(2.0))));
    } catch (MaxIterationsExceededException ex) {
        if (x < (mean - 20 * standardDeviation)) { // JDK 1.5 blows at 38
            return 0;
        } else if (x > (mean + 20 * standardDeviation)) {
            return 1;
        } else {
            throw ex;
        }
    }
}