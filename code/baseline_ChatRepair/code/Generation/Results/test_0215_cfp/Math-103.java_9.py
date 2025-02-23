public double cumulativeProbability(double x) throws MathException {
    if (x - mean < -10 * standardDeviation) {
        // Extreme negative values, cumulative probability is effectively 0
        return 0.0;
    } else if (x - mean > 