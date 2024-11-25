private double calculateAsymptoticPValue(final double Umin,
                                         final int n1,
                                         final int n2)
    throws ConvergenceException, MaxCountExceededException {

    // Overflow protection: Use long to prevent overflow when calculating large product values (n1 * n2)
    final long n1n2prod = (long) n1 * n2;

    // http://en.wikipedia.org/wiki/Mann%E2%80%93Whitney_U#Normal_approximation
    final double EU = ((double) n1n2prod / 2.0);
    final double VarU = (((double) n1n2prod) * (n1 + n2 + 1)) / 12.0;

    // Precision control: Use FastMath.sqrt to ensure accurate square root calculation
    final double sqrtVarU = FastMath.sqrt(VarU);

    final double z = ((Umin - EU) / sqrtVarU);

    final NormalDistribution standardNormal = new NormalDistribution(0, 1);

    return 2 * standardNormal.cumulativeProbability(z);
}