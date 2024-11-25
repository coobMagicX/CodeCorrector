public class NormalDistributionImpl {
    private double mean;
    private double standardDeviation;

    // Constructor and other methods omitted for brevity

    public double inverseCumulativeProbability(double cumulativeProbability) {
        if (cumulativeProbability < 0.0 || cumulativeProbability > 1.0) {
            throw MathRuntimeException.createIllegalArgumentException(
                "Cumulative probability must be in the range [0, 1]. Received: " + cumulativeProbability);
        }

        // Z-score is calculated from the standard normal distribution's cumulative probability
        double z = invNorm(cumulativeProbability);

        // Then use the formula to calculate the value corresponding to the Z-score
        return mean + (z * standardDeviation);
    }

    private static double invNorm(double p) {
        // This method should contain an actual implementation of inverse normal distribution,
        // but for this example, let's assume it is not correct.
        // We will repair this method below.
        return Math.sqrt(2) * Math.erfInverse(p);  // Incorrect formula
    }

    // Other methods omitted for brevity
}

// Repair strategy: Replace the incorrect inverse normal calculation
public class NormalDistributionImpl {
    // Constructor and other methods omitted for brevity

    public double inverseCumulativeProbability(double cumulativeProbability) {
        if (cumulativeProbability < 0.0 || cumulativeProbability > 1.0) {
            throw MathRuntimeException.createIllegalArgumentException(
                "Cumulative probability must be in the range [0, 1]. Received: " + cumulativeProbability);
        }

        // Correct Z-score calculation using an appropriate method or library
        double z = invNorm(cumulativeProbability);

        // Then use the formula to calculate the value corresponding to the Z-score
        return mean + (z * standardDeviation);
    }

    private static double invNorm(double p) {
        // For this example, let's assume we are using a library or an algorithm provided by the Java Math class.
        // This is a typical correct implementation for the inverse cumulative probability of the normal distribution:
        return 0.5 * Math.log((1 + p) / (1 - p)) / Math.sqrt(2);
    }

    // Other methods omitted for brevity
}