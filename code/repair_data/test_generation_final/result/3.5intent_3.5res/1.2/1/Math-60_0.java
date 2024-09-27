public double cumulativeProbability(double x) throws MathException {
    final double dev = x - mean;
    if (Double.isInfinite(x)) {
        if (x == Double.POSITIVE_INFINITY) {
            return 1;
        } else {
            return 0;
        }
    } else if (Double.isNaN(x)) {
        throw new MathException("Input is NaN");
    } else {
        if (x < (mean - 20 * standardDeviation)) {
            return 0;
        } else if (x > (mean + 20 * standardDeviation)) {
            return 1;
        } else {
            try {
                return 0.5 * (1.0 + Erf.erf((dev) / (standardDeviation * FastMath.sqrt(2.0)));
            } catch (MaxIterationsExceededException ex) {
                throw ex;
            }
        }
    }
}