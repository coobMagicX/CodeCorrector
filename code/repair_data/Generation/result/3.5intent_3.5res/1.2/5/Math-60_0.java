public double cumulativeProbability(double x) throws MathException {
    if (x == Double.MAX_VALUE) {
        return 1;
    } else if (x == Double.MIN_VALUE) {
        return 0;
    }

    final double dev = x - mean;
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