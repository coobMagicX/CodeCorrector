import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.moment.Mean;

private double calculateAsymptoticPValue(final double Umin,
                                         final int n1,
                                         final int n2)
    throws ConvergenceException, MaxCountExceededException {

    if (n1 <= 0 || n2 <= 0) {
        throw new IllegalArgumentException("Both sample sizes must be positive.");
    }

    final int n1n2prod = n1 * n2;

    // Calculate the mean and variance for U using the formula from
    // http://en.wikipedia.org/wiki/Mann%E2%80%93Whitney_U#Normal_approximation
    final double EU = n1n2prod / 2.0;
    final double VarU = n1n2prod * (n1 + n2 + 1) / 12.0;

    // Calculate the z-score using the formula
    final double z = (Umin - EU) / FastMath.sqrt(VarU);

    // Use a NormalDistribution to calculate the cumulative probability from the z-score
    final NormalDistribution standardNormal = new NormalDistribution(0, 1);
    final double pValue = standardNormal.cumulativeProbability(z);

    // Return twice the probability of the upper tail since we want the two-tailed p-value
    return 2 * (1 - pValue);
}