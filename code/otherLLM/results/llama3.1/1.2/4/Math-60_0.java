public double cumulativeProbability(double x) throws MathException {
    final double dev = x - mean;
    try {
        return 0.5 * (1.0 + Erf.erf((dev) /
                (standardDeviation * FastMath.sqrt(2.0))));
    } catch (MaxIterationsExceededException ex) {
        // Check for extreme values directly, rather than relying on the erf function's behavior
        if (x < mean - 20 * standardDeviation || x > mean + 20 * standardDeviation) {
            return x < mean - 20 * standardDeviation ? 0 : 1;
        } else {
            throw ex;
        }
    }
}