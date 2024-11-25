public double cumulativeProbability(double x) throws MathException {
    final double dev = x - mean;
    try {
        return 0.5 * (1.0 + Erf.erf((dev) /
                (standardDeviation * FastMath.sqrt(2.0))));
    } catch (MaxIterationsExceededException ex) {
        if (x == Double.POSITIVE_INFINITY) { // Handle positive infinity
            return 1;
        } else if (x == Double.NEGATIVE_INFINITY) { // Handle negative infinity
            return 0;
        } else if (x < (mean - 20 * standardDeviation)) { 
            return 0; 
        } else if (x > (mean + 20 * standardDeviation)) {
            return 1;
        } else {
            throw ex;
        }
    }
}