public double cumulativeProbability(double x) throws MathException {
    if (x < mean) {
        return 0.5 * Erf.erf((x - mean) / (standardDeviation * Math.sqrt(2.0)));
    } else {
        return 1 - 0.5 * Erf.erf((mean - x) / (standardDeviation * Math.sqrt(2.0)));
    }
}