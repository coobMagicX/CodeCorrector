private double calculateAsymptoticPValue(final int Umin,
                                         final int n1,
                                         final int n2)
    throws ConvergenceException, MaxCountExceededException {

    final int n1n2prod = n1 * n2;

    // http://en.wikipedia.org/wiki/Mann%E2%80%93Whitney_U#Normal_approximation
    final double EU = n1n2prod / 2.0;
    final double VarU = n1n2prod * (n1 + n2 + 1) / 12.0;

    // Avoid overflow by using int for Umin and calculate z with long to prevent precision loss
    final long uMinLong = Umin;
    final long EUlong = EU;
    final long VarUlong = VarU;
    final long z = (uMinLong - EUlong) * FastMath.sqrt(VarUlong);

    // Use int for n1n2prod to prevent overflow and long for z to keep precision
    final long n1n2prodl = n1n2prod;

    final NormalDistribution standardNormal = new NormalDistribution(0, 1);

    return 2 * standardNormal.cumulativeProbability((double)z / (double)n1n2prodl);
}