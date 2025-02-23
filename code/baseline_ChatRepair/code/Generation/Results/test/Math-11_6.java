public double density(final double[] vals) throws DimensionMismatchException {
    final int dim = getDimension();
    if (vals.length != dim) {
        throw new DimensionMismatchException(vals.length, dim);
    }

    double factor = 1.0 / (Math.sqrt(Math.pow(2 * Math.PI, dim) * covarianceMatrixDeterminant));
    return factor * getExponentTerm(vals);
}
