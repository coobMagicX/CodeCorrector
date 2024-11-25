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
                double t = Math.abs(r * Math.sqrt((nObs - 2)/(1 - Math.pow(r, 2))));
                // Added handling for overflow/underflow scenarios
                if (Double.isInfinite(t)) {
                    out[i][j] = Double.NaN;
                } else {
                    out[i][j] = 2 * (1 - tDistribution.cumulativeProbability(Math.min(4.5, Math.max(-4.5, t))));
                }
            }
        }
    }
    return new BlockRealMatrix(out);
}