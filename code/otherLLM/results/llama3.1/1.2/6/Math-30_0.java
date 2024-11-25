private double calculateAsymptoticPValue(final double Umin,
                                         final int n1,
                                         final int n2)
    throws ConvergenceException, MaxCountExceededException {

    final int n1n2prod = n1 * n2;

    // http://en.wikipedia.org/wiki/Mann%E2%80%93Whitney_U#Normal_approximation
    final double EU = n1n2prod / 2.0;
    final double VarU = n1n2prod * (n1 + n2 + 1) / 12.0;

    // Check for numerical instability and integer overflow
    if (VarU == 0 || Double.isInfinite(VarU)) {
        throw new ConvergenceException("Numerical instability or integer overflow occurred in asymptotic p-value calculation.");
    }

    final double z = (Umin - EU) / FastMath.sqrt(VarU);

    // Use a more robust method to calculate the cumulative probability
    final NormalDistribution standardNormal = new NormalDistribution(0, 1);
    return 2 * standardNormal.cumulativeProbability(FastMath.signum(z) * Math.acos(Math.abs(z)));
}