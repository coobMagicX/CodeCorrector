private double calculateAsymptoticPValue(final double Umin,
                                         final int n1,
                                         final int n2)
throws ConvergenceException, MaxCountExceededException {

    if (n1 <= 0 || n2 <= 0) {
        throw new IllegalArgumentException("Both sample sizes must be positive.");
    }

    final int n1n2prod = n1 * n2;

    // Check for extreme values or outliers that could lead to incorrect Umin value
    if (Umin < 0 || Umin > n1n2prod) {
        throw new IllegalArgumentException("Umin is out of the valid range [0, n1*n2].");
    }

    final double EU = n1n2prod / 2.0;
    final double VarU = n1n2prod * (n1 + n2 + 1) / 12.0;

    // Use FastMath.sqrt to calculate the square root with higher precision
    final double z = (Umin - EU) / FastMath.sqrt(VarU);

    // Check for numerical stability issues by ensuring that z is not too large or small
    if (Double.isInfinite(z) || Double.isNaN(z)) {
        throw new ConvergenceException("Numerical instability encountered in the calculation of z.");
    }

    final NormalDistribution standardNormal = new NormalDistribution(0, 1);

    // Use cumulativeProbability directly with the calculated z value
    return 2 * standardNormal.cumulativeProbability(z);
}