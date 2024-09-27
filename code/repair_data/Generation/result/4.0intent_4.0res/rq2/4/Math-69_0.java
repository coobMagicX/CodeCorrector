public RealMatrix getCorrelationPValues() throws MathException {
    TDistribution tDistribution = new TDistributionImpl(nObs - 2);
    int nVars = correlationMatrix.getColumnDimension();
    double[][] out = new double[nVars][nVars];
    for (int i = 0; i < nVars; i++) {
        for (int j = 0; j < nVars; j++) {
            if (i == j) {
                out[i][j] = 1.0;  // Diagonal elements should be 1, as p-value for the same variable should be 1 (not significant)
            } else {
                double r = correlationMatrix.getEntry(i, j);
                if (Math.abs(r) == 1) {
                    out[i][j] = 0.0; // Perfect correlation or anti-correlation leads to a p-value of 0 (highly significant)
                } else {
                    double t = Math.abs(r * Math.sqrt((nObs - 2) / (1 - r * r)));
                    out[i][j] = 2 * (1 - tDistribution.cumulativeProbability(t));
                    // Ensure p-value is never exactly zero for non-perfect correlations
                    out[i][j] = Math.max(out[i][j], Double.MIN_VALUE);
                }
            }
        }
    }
    return new BlockRealMatrix(out);
}