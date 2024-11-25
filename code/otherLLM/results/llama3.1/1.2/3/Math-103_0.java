public double cumulativeProbability(double x) throws MathException {
    if (Double.isInfinite(x)) {
        return 1.0; // Return 1 for infinite positive value, adjust accordingly for negative
    }
    if (x < mean - 100 * standardDeviation) { // Adjusted range for very small values
        return 0.0;
    } else if (x > mean + 100 * standardDeviation) { // Adjusted range for very large values
        return 1.0;
    }
    return 0.5 * (1.0 + Erf.erf((x - mean) /
            (standardDeviation * Math.sqrt(2.0))));
}