private double calculateAsymptoticPValue(final double Umin,
                                         final int n1,
                                         final int n2)
    throws ConvergenceException, MaxCountExceededException {

    final int n1n2prod = n1 * n2;

    // http://en.wikipedia.org/wiki/Mann%E2%80%93Whitney_U#Normal_approximation
    final double EU = (n1 * n2) / 2.0; // Corrected formula for EU
    final double VarU = ((n1 + n2 + 1) * n1n2prod) / 12.0;

    final double z = ((Umin - EU) / FastMath.sqrt(VarU)); // Normalized z-score

    final NormalDistribution standardNormal = new NormalDistribution(0, 1);

    return 2 * (1 - standardNormal.cumulativeProbability(Math.abs(z))); // Corrected calculation for p-value
}