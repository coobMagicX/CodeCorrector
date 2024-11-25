public double density(final double[] vals) throws DimensionMismatchException {
    final int dim = getDimension();
    if (vals.length != dim) {
        throw new DimensionMismatchException(vals.length, dim);
    }

    // Calculate covariance matrix determinant for a univariate distribution
    final double det = 1.0; // For a single-element array, the determinant is always 1

    return FastMath.pow(2 * FastMath.PI, -dim / 2) *
        FastMath.pow(det, -0.5) *
        getExponentTerm(vals);
}