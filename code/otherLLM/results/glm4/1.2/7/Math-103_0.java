public class ErfCalculator {

    private double mean;
    private double standardDeviation;

    public void setStandardDeviation(double sd) {
        if (sd <= 0.0) {
            throw new IllegalArgumentException(
                "Standard deviation must be positive.");
        }
        this.standardDeviation = sd;
    }

    public double getMean() {
        return mean;
    }

    public double cumulativeProbability(double x) throws MathException {
        // Check for extreme positive or negative values
        if (Double.isInfinite(x) || Double.isNaN(x)) {
            throw new MathException("Input value is out of range.");
        }
        
        // Use the absolute value to avoid issues with negative standard deviation
        double z = Math.abs((x - mean) / (standardDeviation * Math.sqrt(2.0)));

        // Calculate the error function using a safe method or an approximation if necessary
        return 0.5 * (1.0 + erf(z));
    }

    private double erf(double z) {
        // Use an existing implementation of the error function.
        // This is typically a library method that handles all edge cases correctly.
        // For example, using MathErrorFunction as a placeholder for the actual error function.
        return MathErrorFunction.erf(z);
    }
}

// Placeholder class for MathErrorFunction to simulate an existing erf implementation
class MathErrorFunction {
    public static double erf(double z) {
        // This is just a stub. The actual implementation of Erf should be imported here.
        // For example, it could use the Apache Commons Math library or similar.
        return 0; // Replace with the correct implementation.
    }
}