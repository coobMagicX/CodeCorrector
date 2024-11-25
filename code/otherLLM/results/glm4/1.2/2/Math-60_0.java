public double cumulativeProbability(double x) throws MathException {
    final double dev = x - mean;
    try {
        return 0.5 * (1.0 + Erf.erf((dev) / (standardDeviation * FastMath.sqrt(2.0))));
    } catch (MaxIterationsExceededException ex) {
        // Adjusting the threshold for handling extreme values more robustly
        if (Double.isInfinite(dev)) {
            return dev > 0 ? 1.0 : 0.0;
        }
        if (x < (mean - 20 * standardDeviation)) { // JDK 1.5 blows at 38
            return 0;
        } else if (x > (mean + 20 * standardDeviation)) {
            return 1;
        } else {
            throw ex;
        }
    }
}