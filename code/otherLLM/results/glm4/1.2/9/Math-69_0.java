public RealMatrix getCorrelationPValues() throws MathException {
    TDistribution tDistribution = new TDistributionImpl(nObs - 2);
    int nVars = correlationMatrix.getColumnDimension();
    double[][] out = new double[nVars][nVars];
    
    // Calculate the expected p-value for perfect correlations (which should be zero)
    double expectedPValueForPerfectCorrelation = 0.0;
    if (nObs > 2) {
        expectedPValueForPerfectCorrelation = tDistribution.cumulativeProbability(0);
    }
    
    for (int i = 0; i < nVars; i++) {
        for (int j = 0; j < nVars; j++) {
            double r = correlationMatrix.getEntry(i, j);
            if (i == j) {
                // Diagonal elements should be zero
                out[i][j] = expectedPValueForPerfectCorrelation;
            } else {
                double t = Math.abs(r * Math.sqrt((nObs - 2) / (1 - r * r)));
                double pValue = 2 * (1 - tDistribution.cumulativeProbability(t));
                
                // For perfect correlation, we should get a p-value close to zero
                if (Math.abs(r) == 1.0) {
                    out[i][j] = expectedPValueForPerfectCorrelation;
                } else {
                    out[i][j] = pValue;
                }
            }
        }
    }
    
    return new BlockRealMatrix(out);
}