public double cumulativeProbability(double x) throws MathException {
    final double dev = x - mean;
    
    if (Double.isInfinite(dev)) { // Handle edge cases for infinite values
        return Double.isPositiveInfinity(dev) ? 1 : 0; // For positive infinity, return 1; for negative infinity, return 0
    } else if (Math.abs(dev) > 20 * standardDeviation) { // Check for extreme values
        return Math.abs(dev) >= standardDeviation ? 
               (dev >= 0 ? 1 : 0) : 
               0.5 * (1.0 + Erf.erf((dev) / (standardDeviation * FastMath.sqrt(2.0))));
    } else {
        try {
            return 0.5 * (1.0 + Erf.erf((dev) / (standardDeviation * FastMath.sqrt(2.0))));
        } catch (MaxIterationsExceededException ex) {
            throw ex;
        }
    }
}