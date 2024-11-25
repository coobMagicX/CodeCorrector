public double cumulativeProbability(double x) throws MathException {
    final double dev = x - mean;
    try {
        // Handle extreme values by using the density function for large/small arguments
        if (Math.abs(dev) > 20 * standardDeviation) { 
            return (dev < 0) ? 0 : 1; 
        }
        
        // Use Erf.erf only when |dev| <= 20*standardDeviation
        return 0.5 * (1.0 + Erf.erf((dev) /
                (standardDeviation * FastMath.sqrt(2.0))));
    } catch (MaxIterationsExceededException ex) {
        if (x < (mean - 20 * standardDeviation)) { 
            return 0;
        } else if (x > (mean + 20 * standardDeviation)) {
            return 1;
        } else {
            throw ex;
        }
    }
}