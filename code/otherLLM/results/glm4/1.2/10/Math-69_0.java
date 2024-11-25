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
                // Calculate the t-statistic with proper handling of the term inside sqrt
                double denominator = Math.sqrt((nObs - 2) * (1 - r * r));
                double t = Double.NaN; // Initialize to handle division by zero or sqrt of negative number
                
                if (denominator > 0) {
                    t = Math.abs(r) * Math.sqrt(denominator);
                }
                
                out[i][j] = 2 * (1 - tDistribution.cumulativeProbability(t));
            }
        }
    }
    return new BlockRealMatrix(out);
}