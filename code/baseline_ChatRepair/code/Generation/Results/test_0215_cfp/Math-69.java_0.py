public RealMatrix getCorrelationPValues() throws MathException {
    TDistribution tDistribution = new TDistributionImpl(nObs - 2);
    int nVars = correlationMatrix.getColumnDimension();
    double[][] out = new double[nVars][nVars];
    for (int i = 0; i < nVars; i++) {
        for (int j = 0; j < nVars; j++) {
            if (i == j) {
                out[i][j] = 0d; // Diagonal elements representing correlation of variable with itself
            } else {
                double r = correlationMatrix.getEntry(i, j);
                if (r == 1 || r == -1) {
                    // When r is exactly 1 or -1, p-value should theoretically be 0, but we set it to the smallest possible positive value to avoid log of zero issues
                    out[i][j] = Double.MIN_VALUE;
                } else {
                    double t = Math.abs(r * Math.sqrt((nObs - 2) / (1 - r * r)));
                    double pValue = 2 * (1 - tDistribution.cumulativeProbability(t));
                    out[i][j] = pValue > 0 ? pValue : Double.MIN_VALUE; // Ensure p-value is not exactly zero
                }
            }
        }
    }
    return new BlockRealMatrix(out);
}
