public RealMatrix getCorrelationPValues() throws MathException {
    TDistribution tDistribution = new TDistributionImpl(nObs - 2);
    int nVars = correlationMatrix.getColumnDimension();
    double[][] out = new double[nVars][nVars];
    for (int i = 0; i < nVars; i++) {
        for (int j = 0; j < nVars; j++) {
            if (i == j) {
                out[i][j] = 0.0d; 
            } else {
                double r = correlationMatrix.getEntry(i, j);
                
                if (Math.abs(r) == 1) {
                    out[i][j] = 0.0; 
                } else {
                    double t = Math.abs(r * Math.sqrt((nObs - 2) / (1 - r * r)));
                    double pValue = 2 * (1 - tDistribution.cumulativeProbability