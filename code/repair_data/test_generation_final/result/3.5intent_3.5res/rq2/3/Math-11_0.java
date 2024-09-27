public double density(final double[] vals) throws DimensionMismatchException {
    final int dim = getDimension();
    if (vals.length != dim) {
        throw new DimensionMismatchException(vals.length, dim);
    }

    return Math.pow(2 * Math.PI, -dim / 2) *
        Math.pow(covarianceMatrixDeterminant, -0.5) *
        getDensity(vals);
}