public double density(final double[] vals) throws DimensionMismatchException {
    final int dim = getDimension();
    
    if (dim == 1) { // Check dimensionality and handle univariate case
        return FastMath.exp(-FastMath.pow((vals[0] - mean[0]) / stdDev[0], 2)) /
            (stdDev[0] * Math.sqrt(2 * FastMath.PI));
    } else {
        if (vals.length != dim) {
            throw new DimensionMismatchException(vals.length, dim);
        }

        return FastMath.pow(2 * FastMath.PI, -dim / 2) *
            FastMath.pow(covarianceMatrixDeterminant, -0.5) *
            getExponentTerm(vals);
    }
}