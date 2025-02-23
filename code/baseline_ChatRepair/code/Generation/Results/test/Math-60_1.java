public double cumulativeProbability(double x) throws MathException {
    
    if (x == Double.NEGATIVE_INFINITY) {
        return 0.0;
    } else if (x == Double.POSITIVE_INFINITY) {
        return 1.0;
    }

    final double dev = x - mean;
    if (Math.abs(dev) > 20 * standardDeviation) {
        return dev < 0 ? 0.0 : 1.0;
    }

    try {
        
        return 0.5 * (1.0 + Erf.erf(dev / (standardDeviation * FastMath.sqrt(2.0))));
    } catch (MaxIterationsExceededException ex) {
        