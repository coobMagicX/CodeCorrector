public double cumulativeProbability(double x) {
    final double dev = x - mean;
    try {
        return 0.5 * (1.0 + Erf.erf((dev) / (standardDeviation * FastMath.sqrt(2.0))));
    } catch (MaxIterationsExceededException ex) {
        if (Double.isNaN(dev)) {
            return Double.NaN; // Handle cases where the deviation is NaN
        } else if (Double.isInfinite(dev)) {
            if (dev > 0) {
                return 1;
            } else {
                return 0;
            }
        } else if (Math.abs(dev) >= 20 * standardDeviation) { // Check if |x - mean| is beyond the threshold
            if (dev > 0) {
                return 1;
            } else {
                return 0;
            }
        }
    }
    throw new MathException("Calculation failed due to maximum iterations exceeded");
}

// Note: The method MaxIterationsExceededException has been replaced with MathException as it is not a standard exception in Java.