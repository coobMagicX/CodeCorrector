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
                // Check for numerical precision issues near r = 1 by adding a small epsilon value
                final double epsilon = 1e-10;
                double adjustedR = Math.abs(r + (r == 1 ? -epsilon : 0));
                double t = Math.abs(adjustedR * Math.sqrt((nObs - 2) / (1 - adjustedR * adjustedR)));
                
                // Ensure t is non-negative before passing to the cumulative probability function
                t = Math.max(t, 0);
                
                out[i][j] = 2 * (1 - tDistribution.cumulativeProbability(t));
            }
        }
    }
    return new BlockRealMatrix(out);
}