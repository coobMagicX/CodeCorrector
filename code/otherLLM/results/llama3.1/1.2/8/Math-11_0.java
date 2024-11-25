public double density(final double[] vals) throws DimensionMismatchException {
    final int dim = getDimension();
    if (vals.length != 1) { // Validate dimension consistency for a single point in the univariate distribution
        throw new DimensionMismatchException(vals.length, dim);
    }

    return FastMath.pow(2 * FastMath.PI, -getDimension() / 2) *
        FastMath.exp(-0.5 * getExponentTerm(vals)) * // Review exponent term calculation for a single-element array
        covarianceMatrixDeterminant; // Check covariance matrix determinant calculation for the univariate case
}