public static double inverseCumulativeProbability(double cumulativeProbability) {
    // Constants for the Normal Distribution
    final double mean = 0.0; // Mean of the normal distribution
    final double variance = 1.0; // Variance of the normal distribution

    // The cumulative probability is expected to be between 0 and 1, inclusive.
    if (cumulativeProbability < 0 || cumulativeProbability > 1) {
        throw new IllegalArgumentException("Cumulative probability must be between 0 and 1.");
    }

    // Transforming cumulative probability to standard normal distribution
    double standardCumulativeProbability = 2.0 * cumulativeProbability - 1.0;

    // We need to solve the equation: phi(z) = cumulativeProbability for z (standard score)
    // where phi is the cumulative distribution function of the standard normal distribution.

    // The range bracketing method will be used to find the value of z.
    double initialGuess = 0.5; // Initial guess is in the middle of the range
    double lowerBound = -10;   // Assuming a large enough lower bound
    double upperBound = 10;    // Assuming a large enough upper bound
    int maximumIterations = 100; // Maximum number of iterations allowed

    try {
        double[] bracketPoints = bracket(phi, initialGuess, lowerBound, upperBound, maximumIterations);
        double z = solve(phi, bracketPoints[0], bracketPoints[1], 1e-6); // Solve with a high precision
        return standardScoreToValue(z, mean, variance); // Convert the standard score back to the normal distribution value
    } catch (Exception e) {
        throw new RuntimeException("Failed to compute inverse cumulative probability", e);
    }
}

// The phi function is the CDF of the standard normal distribution.
public static double phi(double x) {
    // Implementing a numerical method to approximate the CDF of the standard normal distribution
    return 0.5 * (1.0 + Math.erf(x / Math.sqrt(2.0)));
}

// Converts a z-score to the corresponding value in the normal distribution.
private static double standardScoreToValue(double z, double mean, double variance) {
    // Using the inverse of the cumulative distribution function for the normal distribution
    return (z * Math.sqrt(variance)) + mean;
}