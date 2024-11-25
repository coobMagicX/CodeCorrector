import static java.lang.Math.*;

public class ProbabilityCalculator {

    // Assuming there is an existing method called erf() that calculates the error function correctly.
    public static double Erf(double value) {
        // Error function implementation, simplified for this example:
        return 2 * Math.sqrt(PI) * (1 - (value * value));
    }

    private double mean;
    private double standardDeviation;

    public ProbabilityCalculator(double mean, double standardDeviation) {
        this.mean = mean;
        this.standardDeviation = standardDeviation;
    }

    public double cumulativeProbability(double x) throws ArithmeticException {
        if (standardDeviation <= 0) {
            throw new ArithmeticException("Standard Deviation must be greater than zero.");
        }
        
        // Calculate z-score
        double zScore = (x - mean) / (standardDeviation * sqrt(2.0));
        
        // Clamp the value of erf to stay within [0, 1]
        double erfResult = Erf(zScore);
        if (erfResult < 0) {
            erfResult = 0;
        } else if (erfResult > 1) {
            erfResult = 1;
        }
        
        return 0.5 * (1 + erfResult);
    }

    // Example usage
    public static void main(String[] args) {
        ProbabilityCalculator calculator = new ProbabilityCalculator(0, 1);
        try {
            double probability = calculator.cumulativeProbability(2.0);
            System.out.println("Cumulative Probability: " + probability);
        } catch (ArithmeticException e) {
            System.err.println("Error calculating cumulative probability: " + e.getMessage());
        }
    }
}