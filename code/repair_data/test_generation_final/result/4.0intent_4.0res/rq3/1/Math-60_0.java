public double cumulativeProbability(double x) throws MathException {
    final double dev = x - mean;
    try {
        // Correct use of Erf class and FastMath class
        return 0.5 * (1.0 + org.apache.commons.math3.special.Erf.erf(dev / (standardDeviation * Math.sqrt(2.0))));
    } catch (MaxIterationsExceededException ex) {
        // Handling extreme cases by checking deviation instead of absolute x
        if (dev < -20 * standardDeviation) { 
            return 0;
        } else if (dev > 20 * standardDeviation) {
            return 1;
        } else {
            throw ex;
        }
    }
}