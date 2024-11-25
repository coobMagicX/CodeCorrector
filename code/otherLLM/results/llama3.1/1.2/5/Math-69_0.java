public RealMatrix getCorrelationPValues() throws MathException {
    TDistribution tDistribution = new TDistributionImpl(nObs - 2);
    int nVars = correlationMatrix.getColumnDimension();
    double[][] out = new double[nVars][nVars];
    checkSufficientData(correlationMatrix); // added to handle edge cases
    
    for (int i = 0; i < nVars; i++) {
        for (int j = 0; j < nVars; j++) {
            if (i == j) {
                out[i][j] = 0d;
            } else {
                double r = correlationMatrix.getEntry(i, j);
                // added numerical stability fix
                double t = Math.abs(r * Math.sqrt((nObs - 2)/(1 + (r > 0 ? 1e-10 : -1e-10))));
                
                out[i][j] = 2 * (1 - tDistribution.cumulativeProbability(t));
            }
        }
    }
    
    return new BlockRealMatrix(out);
}