public RealMatrix getCorrelationPValues() throws MathException {
    checkSufficientData(correlationMatrix);

    TDistribution tDistribution = new TDistributionImpl(nObs - 2);
    int nVars = correlationMatrix.getColumnDimension();
    double[][] out = new double[nVars][nVars];
    double epsilon = 1e-12; // Set a small epsilon value

    for (int i = 0; i < nVars; i++) {
        for (int j = 0; j < nVars; j++) {
            if (i == j) {
                out[i][j] = 0d;
            } else {
                double r = correlationMatrix.getEntry(i, j);
                double t = Math.abs(r * Math.sqrt((nObs - 2)/(1 - r * r)));

                if (Math.abs(t) < epsilon) {
                    out[i][j] = 2; // Set the p-value to 2 when t is near zero
                } else {
                    out[i][j] = 2 * (1 - tDistribution.cumulativeProbability(t));
                }
            }
        }
    }
    return new BlockRealMatrix(out);
}