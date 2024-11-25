public double density(final double[] vals) throws DimensionMismatchException {
    final int dim = getDimension();
    if (vals.length != dim) {
        throw new DimensionMismatchException(vals.length, dim);
    }

    // Assuming the method 'getMultivariateCovarianceMatrixDeterminant' returns the determinant of a covariance matrix
    double det = getMultivariateCovarianceMatrixDeterminant(vals);
    
    return FastMath.pow(2 * FastMath.PI, -dim / 2) *
        FastMath.pow(det, -0.5) *
        getExponentTerm(vals);
}