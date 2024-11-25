public RealMatrix getCorrelationPValues() throws MathException {
    TDistribution tDistribution = new TDistributionImpl(nObs - 2);
    int nVars = correlationMatrix.getColumnDimension();
    double[][] out = new double[nVars][nVars];
    for (int i = 0; i < nVars; i++) {
        for (int j = 0; j < nVars; j++) {
            if (i == j) {
                // Avoid division by zero or very small numbers in calculations
                out[i][j] = Math.min(1e-10, 2 * (1 - tDistribution.cumulativeProbability(1e-10)));
            } else {
                double r = correlationMatrix.getEntry(i, j);
                double t = Math.abs(r * Math.sqrt((nObs - 2)/(1 - r * r)));
                out[i][j] = 2 * (1 - tDistribution.cumulativeProbability(t));
            }
        }
    }
    return new BlockRealMatrix(out);
}