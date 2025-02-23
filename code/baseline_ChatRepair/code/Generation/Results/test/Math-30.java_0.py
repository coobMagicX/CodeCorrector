private double calculateAsymptoticPValue(final double Umin, final int n1, final int n2)
    throws ConvergenceException, MaxCountExceededException {

    final long n1n2prod = (long) n1 * (long) n2;  // Use long to avoid overflow

    // http://en.wikipedia.org/wiki/Mann%E2%80%93Whitney_U#Normal_approximation
    final double EU = n1n2prod / 2.0;
    final double VarU = n1n2prod * (n1 + n2 + 1.0) / 12.0;  // Ensure floating-point division

    final double z = (Umin - EU) / Math.sqrt(VarU); // Use Math.sqrt

    final NormalDistribution standardNormal = new NormalDistribution(0, 1);

    double pValue = standardNormal.cumulativeProbability(z);
    return 2 * (z < 0 ? pValue : 1 - pValue); // Correct asymmetric cumulative probability handling
}
