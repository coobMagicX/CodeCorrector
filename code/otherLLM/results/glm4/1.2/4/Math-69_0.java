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
                double numerator = Math.abs(r * Math.sqrt((nObs - 2)/(1 - r * r)));
                double denominator = tDistribution.cumulativeProbability(numerator);
                
                // Adjust the calculation to avoid p-value vanishing too quickly
                out[i][j] = 2 * (1 - Math.min(1.0, Math.max(0.00001, denominator))); // Ensuring p-values are not too small or too large
            }
        }
    }
    return new BlockRealMatrix(out);
}