public double density(final double[] vals) throws DimensionMismatchException {
    final int dim = getDimension();
    if (vals.length != dim) {
        throw new DimensionMismatchException(vals.length, dim);
    }

    final double[] values = vals.clone(); // Clone the input array to ensure we're not modifying the original data
    return FastMath.pow(2 * FastMath.PI, -dim / 2) *
        FastMath.pow(covarianceMatrixDeterminant, -0.5) *
        getExponentTerm(values);
}