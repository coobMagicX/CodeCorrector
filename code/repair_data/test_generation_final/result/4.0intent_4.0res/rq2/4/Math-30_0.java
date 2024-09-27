private double calculateAsymptoticPValue(final double Umin,
                                         final int n1,
                                         final int n2)
    throws ConvergenceException, MaxCountExceededException {

    final int n1n2prod = n1 * n2;

    // http://en.wikipedia.org/wiki/Mann%E2%80%93Whitney_U#Normal_approximation
    final double EU = n1n2prod / 2.0;
    final double VarU = n1n2prod * (n1 + n2 + 1) / 12.0;

    final double z = (Umin - EU) / FastMath.sqrt(VarU);

    final NormalDistribution standardNormal = new NormalDistribution(0, 1);

    // Correcting the p-value calculation for a two-tailed test:
    // The previous fix should have correctly addressed the two-tailed nature,
    // but we need to ensure that the cumulative probability is doubled for the absolute value of z,
    // since we are interested in both tails of the distribution.
    double pValue = 2 * (1 - standardNormal.cumulativeProbability(Math.abs(z)));

    // Ensure p-value does not exceed 1
    return Math.min(pValue, 1.0);
}