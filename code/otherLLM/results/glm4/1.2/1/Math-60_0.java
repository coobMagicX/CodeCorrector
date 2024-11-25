public double cumulativeProbability(double x) throws MathException {
    final double dev = x - mean;
    try {
        // Ensure that the calculation of erf is not dividing by zero or a negative number
        if (standardDeviation <= 0) {
            throw new IllegalArgumentException("Standard deviation must be positive.");
        }
        // Calculate cumulative probability using the density function and integrate it from the negative infinity to x
        double integral = -1; // Start with an invalid value for integration
        for (double i = Double.NEGATIVE_INFINITY; i <= x; i += 0.01) { // Increment step size as needed for accuracy
            integral += density(i);
        }
        return 2 * integral;
    } catch (MaxIterationsExceededException ex) {
        if (x < mean - 20 * standardDeviation) { // JDK 1.5 blows at 38
            return 0;
        } else if (x > mean + 20 * standardDeviation) {
            return 1;
        } else {
            throw ex;
        }
    }
}