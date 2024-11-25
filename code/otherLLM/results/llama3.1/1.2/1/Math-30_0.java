private double calculateAsymptoticPValue(final double Umin,
                                         final int n1,
                                         final int n2)
    throws ConvergenceException, MaxCountExceededException {

    final int n1n2prod = Math.min(n1 * n2, Integer.MAX_VALUE); // prevent overflow

    // http://en.wikipedia.org/wiki/Mann%E2%80%93Whitney_U#Normal_approximation
    final double EU = Math.min(n1n2prod / 2.0, Double.MAX_VALUE); // prevent underflow
    final double VarU = n1n2prod * (Math.min(n1 + 1, Integer.MAX_VALUE) + Math.min(n2 + 1, Integer.MAX_VALUE)) / 12.0;

    final double z = (Umin - EU) / FastMath.sqrt(Math.max(VarU, Double.MIN_NORMAL)); // prevent underflow

    final NormalDistribution standardNormal = new NormalDistribution(0, 1);

    return 2 * standardNormal.cumulativeProbability(z);
}