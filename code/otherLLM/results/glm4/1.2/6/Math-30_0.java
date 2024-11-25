private double calculateAsymptoticPValue(final double Umin,
                                         final int n1,
                                         final int n2)
        throws ConvergenceException, MaxCountExceededException {

    if (n1 < 20 || n2 < 20) {
        // If either sample size is less than 20, use the exact method or raise an error
        throw new IllegalArgumentException("Normal approximation requires both samples to have at least 20 observations.");
    }

    final int n1n2prod = n1 * n2;

    // http://en.wikipedia.org/wiki/Mann%E2%80%93Whitney_U#Normal_approximation
    final double EU = n1n2prod / 2.0;
    final double VarU = n1n2prod * (n1 + n2 + 1) / 12.0;

    final double z = (Umin - EU) / FastMath.sqrt(VarU);

    final NormalDistribution standardNormal = new NormalDistribution(0, 1);

    // Since the U statistic is symmetric around its mean, we can use a two-tailed test
    return 2 * standardNormal.cumulativeProbability(-FastMath.abs(z));
}