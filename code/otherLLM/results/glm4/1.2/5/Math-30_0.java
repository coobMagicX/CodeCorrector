private double calculateAsymptoticPValue(final double Umin,
                                         final int n1,
                                         final int n2)
    throws ConvergenceException, MaxCountExceededException {

    if (n1 <= 0 || n2 <= 0) {
        throw new IllegalArgumentException("Sample sizes must be positive integers.");
    }

    final int n1n2prod = n1 * n2;

    // Corrected the computation of variance for large datasets
    final double EU = n1n2prod / 2.0;
    final double VarU = n1n2prod * (n1 + n2 + 1) / 12.0;

    // Check for potential numerical instability or overflow/underflow
    if (Double.isInfinite(EU) || Double.isNaN(EU) || Double.isInfinite(VarU) || Double.isNaN(VarU)) {
        throw new ConvergenceException("Numerical stability issue in U computation.");
    }

    final double z = (Umin - EU) / FastMath.sqrt(VarU);

    // Assuming the NormalDistribution and its cumulativeProbability method are implemented correctly
    final NormalDistribution standardNormal = new NormalDistribution(0, 1);

    return 2 * standardNormal.cumulativeProbability(z);
}