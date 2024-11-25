import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;

private double calculateAsymptoticPValue(final double Umin,
                                         final int n1,
                                         final int n2)
throws ConvergenceException, MaxCountExceededException {

    final int n1n2prod = n1 * n2;
    
    // Calculate the expected value and variance for the Mann-Whitney U statistic
    final double EU = n1n2prod / 2.0;
    final double VarU = (n1n2prod * (n1 + n2 + 1)) / 12.0;

    // Calculate z-score based on the asymptotic approximation of the standard normal distribution
    final double z = (Umin - EU) / Math.sqrt(VarU);

    // Create a new instance of NormalDistribution to calculate the cumulative probability
    final NormalDistribution standardNormal = new NormalDistribution(0, 1);
    
    // The p-value is twice the area under the curve to the left of the z-score for the Umin value
    return 2 * standardNormal.cumulativeProbability(z);
}

// Example usage with a Mann-Whitney U test instance
public double calculatePValueExample(double[] sample1, double[] sample2) {
    MannWhitneyUTest uTest = new MannWhitneyUTest();
    try {
        // Perform the test and get the p-value
        return uTest.mannWhitneyU(sample1, sample2);
    } catch (IllegalArgumentException e) {
        System.err.println("Illegal argument: " + e.getMessage());
        return Double.NaN;
    }
}