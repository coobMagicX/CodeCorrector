public double cumulativeProbability(double x) throws MathException {
    final double dev = x - mean;
    try {
        return 0.5 * (1.0 + Erf.erf((dev) /
                (getStandardDeviation() * FastMath.sqrt(2.0))));
    } catch (MaxIterationsExceededException ex) {
        if (x < (mean - 40 * getStandardDeviation())) { // Adjusted for extreme values
            return 0;
        } else if (x > (mean + 40 * getStandardDeviation())) { // Adjusted for extreme values
            return 1;
        } else {
            throw ex;
        }
    }
}