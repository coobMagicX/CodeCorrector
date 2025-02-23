public double cumulativeProbability(double x) throws MathException {
    // Calculate z-value
    double z = (x - mean) / (standardDeviation * Math.sqrt(2.0));

    // Check for extreme z-values to handle them without error function overflow
    if (z < -10) {
        // If the z-value is too low, the cumulative probability approaches 0 closely
        return 0.0;
    } else if (z > 10) {
        // If the z-value is too high, the cumulative probability approaches 1 closely
       