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
                // Adding Math.max to avoid negative values in the denominator
                double denominator = Math.max(1e-8, 1 - r * r); // Prevent division by a very small number
                double t = Math.abs(r) * Math.sqrt((nObs - 2) / denominator);
                out[i][j] = 2 * (1 - tDistribution.cumulativeProbability(t));
            }
        }
    }
    return new BlockRealMatrix(out);
}