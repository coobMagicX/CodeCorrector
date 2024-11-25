public RealMatrix getCorrelationPValues() throws MathException {
    checkSufficientData(correlationMatrix); // Ensuring there is sufficient data

    TDistribution tDistribution = new TDistributionImpl(nObs - 2);
    int nVars = correlationMatrix.getColumnDimension();
    double[][] out = new double[nVars][nVars];
    for (int i = 0; i < nVars; i++) {
        for (int j = 0; j < nVars; j++) {
            if (i == j) {
                out[i][j] = 0d;
            } else {
                double r = correlationMatrix.getEntry(i, j);
                // Ensuring the denominator is not zero to avoid division by zero error
                double denominator = Math.sqrt(1 - r * r);
                if (denominator == 0) {
                    throw MathRuntimeException.createIllegalArgumentException(
                            "invalid data: denominator became zero while calculating t value for correlation matrix.");
                }
                double t = Math.abs(r * Math.sqrt((nObs - 2) / denominator));
                out[i][j] = 2 * (1 - tDistribution.cumulativeProbability(t));
            }
        }
    }
    return new BlockRealMatrix(out);
}