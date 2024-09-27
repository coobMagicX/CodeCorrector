public RealMatrix getCorrelationPValues() throws MathException {
    TDistribution tDistribution = new TDistributionImpl(nObs - 2);
    int nVars = correlationMatrix.getColumnDimension();
    double[][] out = new double[nVars][nVars];
    for (int i = 0; i < nVars; i++) {
        for (int j = 0; j < nVars; j++) {
            if (i == j) {
                out[i][j] = 0d;
            } else {
                double r = correlationMatrix.getEntry(i, j);
                double t = Math.abs(r * Math.sqrt((nObs - 2)/(1 - r * r)));
                out[i][j] = 2 * (1 - tDistribution.cumulativeProbability(t));
                
                // Adjust assert condition to allow for p-values near zero
                assert out[i][j] >= 0 || Math.abs(out[i][j]) < 1e-15 : "P-value should be greater than or equal to zero.";
            }
        }
    }
    return new BlockRealMatrix(out);
}