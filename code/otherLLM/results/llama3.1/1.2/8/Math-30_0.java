private double calculateAsymptoticPValue(final int Umin,
                                         final int n1,
                                         final int n2)
throws ConvergenceException, MaxCountExceededException {

final long n1n2prod = (long) n1 * n2; // Cast to prevent overflow

// http://en.wikipedia.org/wiki/Mann%E2%80%93Whitney_U#Normal_approximation
final double EU = (double) n1n2prod / 2.0;
final double VarU = (double) n1n2prod * (n1 + n2 + 1) / 12.0;

// Perform range check to prevent underflow in calculation
if (VarU <= FastMath.EPSILON) {
    return 1; // or a suitable default value, depending on the context
}

final double z = (Umin - EU) / FastMath.sqrt(VarU);

final NormalDistribution standardNormal = new NormalDistribution(0, 1);

return 2 * standardNormal.cumulativeProbability(z);
}