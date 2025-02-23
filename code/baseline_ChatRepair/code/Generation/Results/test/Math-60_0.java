public double cumulativeProbability(double x) throws MathException {
    
    if (x < mean - 20 * standardDeviation) {
        return 0.0;
    } else if (x > mean + 20 * standardDeviation) {
        return 1.0;
    }

    
    if (Double.isInfinite(x)) {
        return x < 0 ? 0.0 : 1.0;
    }

    final double dev = x - mean;
    try {
        return 0.5 * (1.0 + Erf.erf(dev / (standardDeviation * FastMath.sqrt(2.0))));
    } catch (MaxIterationsExceededException ex) {
        
        if (dev