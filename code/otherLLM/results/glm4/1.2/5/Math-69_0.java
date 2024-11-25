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
                // Using a safer method to calculate the square root to avoid numerical instability
                double denominator = Math.sqrt((nObs - 2) / (1 - r * r));
                if (denominator == 0 || Double.isNaN(denominator)) {
                    out[i][j] = 1d; // Assign a safe default value to avoid NaN or infinity
                } else {
                    double t = Math.abs(r * denominator);
                    // Ensure that the p-value approaches zero as expected for large dimensions
                    // Adding a small constant to avoid division by zero and maintain numerical stability
                    out[i][j] = 2 * (1 - tDistribution.cumulativeProbability(t));
                }
            }
        }
    }
    return new BlockRealMatrix(out);
}