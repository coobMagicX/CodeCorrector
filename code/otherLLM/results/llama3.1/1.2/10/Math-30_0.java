private double calculateAsymptoticPValue(final double Umin,
                                         final int n1,
                                         final int n2)
    throws ConvergenceException, MaxCountExceededException {

    // Input validation: Check for negative values
    if (n1 < 0 || n2 < 0) {
        throw new IllegalArgumentException("Both n1 and n2 must be non-negative");
    }

    final int n1n2prod = n1 * n2;

    // http://en.wikipedia.org/wiki/Mann%E2%80%93Whitney_U#Normal_approximation
    final double EU = n1n2prod / 2.0;
    final double VarU = n1n2prod * (n1 + n2 + 1) / 12.0;

    // Handle boundary value: Avoid division by zero and ensure stability
    if (VarU == 0 || Double.isNaN(VarU)) {
        return 1; // or any other suitable default value for this case
    }

    final double z = (Umin - EU) / FastMath.sqrt(Math.abs(VarU)); // Ensure VarU is not zero

    final NormalDistribution standardNormal = new NormalDistribution(0, 1);

    return 2 * standardNormal.cumulativeProbability(z);
}