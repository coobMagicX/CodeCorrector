public double cumulativeProbability(double x) throws MathException {
    final double dev = x - mean;
    try {
        return 0.5 * (1.0 + Erf.erf(dev / (getStandardDeviation() * FastMath.sqrt(2.0))));
    } catch (MaxIterationsExceededException ex) {
        // Adjusted the multiplier from 20 to a higher threshold to handle more extreme values
        if (x < (mean - 40 * getStandardDeviation())) { // Adjusted to handle more extreme values
            return 0;
        } else if (x > (mean + 40 * getStandardDeviation())) {
            return 1;
        } else {
            throw ex;
        }
    }
}