public double cumulativeProbability(double x) throws MathException {
    double scaledX = (x - mean) / (standardDeviation * Math.sqrt(2.0));
    // Check for extreme values to handle underflow or overflow
    if (scaledX > 5) {
        return 1.0; // Extreme positive values should return 1
    } else if (scaledX < -5) {
        return 0.0; // Extreme negative values should return 0
    } else {
        return 0.5 * (1.0 + Erf.erf(scaledX));
    }
}