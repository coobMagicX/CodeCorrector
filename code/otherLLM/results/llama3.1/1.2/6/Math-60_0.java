public double cumulativeProbability(double x) throws MathException {
    final double dev = x - mean;
    try {
        if (Double.isInfinite(x)) { // added check for infinite values
            return Double.NaN; // or any other suitable value
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