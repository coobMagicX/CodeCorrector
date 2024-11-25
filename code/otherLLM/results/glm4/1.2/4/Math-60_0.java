public double cumulativeProbability(double x) {
    final double dev = x - mean;
    
    // Directly return 0 or 1 for extreme positive or negative deviations
    if (Double.isInfinite(dev)) {
        return dev > 0 ? 1 : 0;
    }
    
    try {
        return 0.5 * (1.0 + Erf.erf((dev) / (standardDeviation * FastMath.sqrt(2.0))));
    } catch (MaxIterationsExceededException ex) {
        if (x < mean - 20 * standardDeviation || x > mean + 20 * standardDeviation) {
            // For extreme values that cause convergence issues, return the boundary probability
            return dev <= 0 ? 1 : 0;
        } else {
            throw ex; // Rethrow the exception for other cases within the range where erf should be used
        }
    }
}