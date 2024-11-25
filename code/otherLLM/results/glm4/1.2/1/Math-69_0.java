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
                double standardError = Math.sqrt((nObs - 2) / (1 - r * r));
                // Avoiding overflow by clamping the value of t to be within a reasonable range.
                double t = Math.abs(r * standardError);
                if (t > 6.0) { // Assuming that |t| should not exceed 6 based on t-distribution properties
                    out[i][j] = 1d; // As t approaches infinity, p-value approaches 0, which we simulate by setting it to 1.
                } else {
                    out[i][j] = 2 * (1 - tDistribution.cumulativeProbability(t));
                }
            }
        }
    }
    
    return new BlockRealMatrix(out);
}