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
                // Prevent underflow by adding a small value to the expression
                out[i][j] = 2 * (tDistribution.cumulativeProbability(t) + 1e-12); // 1e-12 is a small positive value added to prevent underflow
            }
        }
    }
    return new BlockRealMatrix(out);
}