private double calculateAsymptoticPValue(final double Umin,
                                         final int n1,
                                         final int n2)
    throws ConvergenceException, MaxCountExceededException {

    final int n1n2prod = n1 * n2;

    // http://en.wikipedia.org/wiki/Mann%E2%80%93Whitney_U#Normal_approximation
    final double EU = n1n2prod / 2.0;
    final double VarU = n1n2prod * (n1 + n2 + 1) / 12.0;

    final double z = (Umin - EU) / FastMath.sqrt(VarU);

    final NormalDistribution standardNormal = new NormalDistribution(0, 1);

    return 2 * standardNormal.cumulativeProbability(z);
}

Methods that you can utlize:
public double mannWhitneyU(final double[] x, final double[] y)
    throws NullArgumentException, NoDataException {

    ensureDataConformance(x, y);

    final double[] z = concatenateSamples(x, y);
    final double[] ranks = naturalRanking.rank(z);

    double sumRankX = 0;

    /*
     * The ranks for x is in the first x.length entries in ranks because x
     * is in the first x.length entries in z
     */
    for (int i = 0; i < x.length; ++i) {
        sumRankX += ranks[i];
    }

    /*
     * U1 = R1 - (n1 * (n1 + 1)) / 2 where R1 is sum of ranks for sample 1,
     * e.g. x, n1 is the number of observations in sample 1.
     */
    final double U1 = sumRankX - (x.length * (x.length + 1)) / 2;

    /*
     * It can be shown that U1 + U2 = n1 * n2
     */
    final double U2 = x.length * y.length - U1;

    return FastMath.max(U1, U2);
}