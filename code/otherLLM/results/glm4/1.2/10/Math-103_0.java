public class ProbabilityCalculator {

    private double mean;
    private double standardDeviation;

    // Other methods (setStandardDeviation, getStandardDeviation, setMean, getMean) are assumed to be defined here

    public double cumulativeProbability(double x) throws MathException {
        if (Double.isNaN(x) || Double.isInfinite(x)) {
            throw new IllegalArgumentException("Input value must not be NaN or Infinite.");
        }
        return 0.5 * (1.0 + Erf.erf((x - mean) / (getStandardDeviation() * Math.sqrt(2.0))));
    }

    // Assume Erf class and its erf method are defined elsewhere in the context
}

// Repair strategy explanation:
// 1. Added a check for NaN or Infinite values of x to avoid errors when calculating the error function.
// 2. Used getStandardDeviation() instead of direct variable access to ensure encapsulation.