import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.distribution.NormalDistribution;

private double calculateAsymptoticPValue(final double Umin,
                                         final int n1,
                                         final int n2)
    throws ConvergenceException, MaxCountExceededException {

    if (n1 <= 0 || n2 <= 0) {
        throw new IllegalArgumentException("Both sample sizes must be greater than zero.");
    }

    final int n1n2prod = n1 * n2;

    // http://en.wikipedia.org/wiki/Mann%E2%80%93Whitney_U#Normal_approximation
    final double EU = n1n2prod / 2.0;
    final double VarU = n1n2prod * (n1 + n2 + 1) / 12.0;

    // Ensure Umin is within the valid range for the normal approximation
    if (Umin < 0 || Umin > n1n2prod) {
        throw new IllegalArgumentException("The value of Umin must be in the range [0, n1*n2].");
    }

    final double z = (Umin - EU) / Math.sqrt(VarU);

    final NormalDistribution standardNormal = new NormalDistribution(0, 1);

    // Use cumulative probability to calculate p-value
    return 2 * (1 - standardNormal.cumulativeProbability(z));
}

// Example usage of the method:
// double Umin = ...; // Value of Umin from data
// int n1 = ...; // Sample size for group 1
// int n2 = ...; // Sample size for group 2
// try {
//     double pValue = calculateAsymptoticPValue(Umin, n1, n2);
//     if (pValue > 0.1) {
//         System.out.println("The result is statistically significant.");
//     } else {
//         System.out.println("The result is not statistically significant.");
//     }
// } catch (Exception e) {
//     System.err.println(e.getMessage());
// }