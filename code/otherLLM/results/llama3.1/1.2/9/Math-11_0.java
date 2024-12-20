public double density(final double[] vals) throws DimensionMismatchException {
    final int dim = getDimension();
    if (vals.length != dim) {
        throw new DimensionMismatchException(vals.length, dim);
    }

    return FastMath.pow(2 * FastMath.PI, -1 / 2) *
        FastMath.exp(-0.5 * vals[0] * vals[0] / covarianceMatrixDeterminant) *
        getExponentTerm(vals);
}