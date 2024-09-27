public double cumulativeProbability(double x) throws MathException {
    final double dev = x - mean;
    try {
        if (dev == Double.POSITIVE_INFINITY) {
            return 1;
        } else if (dev == Double.NEGATIVE_INFINITY) {
            return 0;
        } else {
            return 0.5 * (1.0 + Erf.erf(dev / (getStandardDeviation() * FastMath.sqrt(2.0))));
        }
    } catch (MaxIterationsExceededException ex) {
        if (x < (mean - 20 * getStandardDeviation())) { // JDK 1.5 blows at 38
            return 0;
        } else if (x > (mean + 20 * getStandardDeviation())) {
            return 1;
        } else {
            throw ex;
        }
    }
}