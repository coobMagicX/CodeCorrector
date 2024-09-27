public RealMatrix getCorrelationPValues() throws MathException {
    checkSufficientData(correlationMatrix);

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
                // Adjust p-value to ensure it is never exactly zero
                if (out[i][j] == 0d) {
                    out[i][j] = Double.MIN_VALUE;
                }
            }
        }
    }
    return new BlockRealMatrix(out);
}