public RealMatrix getCorrelationPValues() throws MathException {
    TDistribution tDistribution = new TDistributionImpl(nObs - 2);
    int nVars = correlationMatrix.getColumnDimension();
    double[][] out = new double[nVars][nVars];
    for (int i = 0; i < nVars; i++) {
        for (int j = 0; j < nVars; j++) {
            if (i == j) {
                out[i][j] = Double.NaN;  // Adjusting to NaN for diagonal elements where correlation with itself is not applicable
            } else {
                double r = correlationMatrix.getEntry(i, j);
                if (r == 1 || r == -1) {
                    out[i][j] = 0.0;  // Perfect correlation or anti-correlation leads to p-value of 0
                } else {
                    double t = Math.abs(r * Math.sqrt((nObs - 2) / (1 - r * r)));
                    double pValue = 2 * (1 - tDistribution.cumulativeProbability(t));
                    out[i][j] = (pValue < Double.MIN_VALUE) ? Double.MIN_VALUE : pValue;  // Ensuring p-values do not round to zero
                }
            }
        }
    }
    return new BlockRealMatrix(out);
}