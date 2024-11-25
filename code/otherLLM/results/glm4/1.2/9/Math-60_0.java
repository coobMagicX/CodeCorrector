public double cumulativeProbability(double x) throws MathException {
    final double dev = x - mean;
    if (Double.isInfinite(dev) || Double.isNaN(dev)) {
        // Handle the case where 'x' is infinity or NaN to avoid infinite or NaN values during calculations.
        return dev < 0 ? 0 : 1; // For positive infinity, return 1 (all values are less than x), for negative infinity, return 0
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