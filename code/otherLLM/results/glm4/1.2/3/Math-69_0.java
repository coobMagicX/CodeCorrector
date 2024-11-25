public RealMatrix getCorrelationPValues() throws MathException {
    TDistribution tDistribution = new TDistributionImpl(nObs - 2);
    int nVars = correlationMatrix.getColumnDimension();
    double[][] out = new double[nVars][nVars];
    for (int i = 0; i < nVars; i++) {
        for (int j = 0; j < nVars; j++) {
            if (i == j) {
                out[i][j] = 1d;
            } else {
                double r = correlationMatrix.getEntry(i, j);
                double denominator = 1 - r * r;

                // Check for numerical stability: avoid division by a small number
                if (denominator < 0.000001) {
                    out[i][j] = Double.NaN; // Assign NaN if the result is not meaningful
                } else {
                    double t = Math.abs(r * Math.sqrt((nObs - 2) / denominator));
                    out[i][j] = 2 * (1 - tDistribution.cumulativeProbability(t));
                }
            }
        }
    }
    return new BlockRealMatrix(out);
}