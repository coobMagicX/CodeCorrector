import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

private double calculateAsymptoticPValue(final double Umin,
                                         final int n1,
                                         final int n2)
    throws ConvergenceException, MaxCountExceededException {

    final int n1n2prod = n1 * n2;

    // http://en.wikipedia.org/wiki/Mann%E2%80%93Whitney_U#Normal_approximation
    final double EU = n1n2prod / 2.0;
    final double VarU = n1n2prod * (n1 + n2 + 1) / 12.0;

    // Calculate the Z-score using the formula for the normal approximation to the Mann-Whitney U statistic
    final double z = (Umin - EU) / Math.sqrt(VarU);

    // Initialize a NormalDistribution object with mean 0 and standard deviation 1.
    final NormalDistribution standardNormal = new NormalDistribution(0, 1);
    
    // Use the cumulative probability distribution function (CDF) to calculate the p-value.
    return 2 * (1 - standardNormal.cumulativeProbability(z));
}