import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

private double calculateAsymptoticPValue(final double Umin,
                                         final int n1,
                                         final int n2)
throws ConvergenceException, MaxCountExceededException {

    final int n1n2prod = n1 * n2;

    // http://en.wikipedia.org/wiki/Mann%E2%80%93Whitney_U#Normal_approximation
    final double EU = n1n2prod / 2.0;
    final double VarU = n1n2prod * (n1 + n2 + 1) / 12.0;

    // Use DescriptiveStatistics to calculate the z-score
    DescriptiveStatistics stats = new DescriptiveStatistics();
    stats.add(Umin - EU);
    double z = stats.getMean();

    // If there is only one observation, use a simpler method for calculating the p-value
    if (n1 == 1 || n2 == 1) {
        return Math.max(0.0, (Umin > 0 ? 1 : 0));
    }

    final NormalDistribution standardNormal = new NormalDistribution(0, 1);

    // Calculate the cumulative probability for the z-score
    double pValue = standardNormal.cumulativeProbability(z);
    
    // Adjust for the two-tailed test by multiplying by 2
    return 2 * pValue;
}