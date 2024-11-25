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
                // Correcting the calculation of t by using Math.pow to avoid precision issues with sqrt
                double denominator = Math.sqrt(Math.pow(1 - r * r, 2));
                double t = Math.abs(r) * Math.sqrt((nObs - 2) / denominator);
                out[i][j] = 2 * (1 - tDistribution.cumulativeProbability(t));
            }
        }
    }
    return new BlockRealMatrix(out);
}