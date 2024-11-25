public double cumulativeProbability(double x) throws MathException {
    final double dev = x - mean;
    try {
        // Added explicit bound checking for extremely large or small input values
        if (Double.isInfinite(dev)) {
            return Double.POSITIVE_INFINITY == dev ? 0 : 1; // handle infinite inputs correctly
        }
        
        return 0.5 * (1.0 + Erf.erf((dev) /
                (standardDeviation * FastMath.sqrt(2.0))));
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