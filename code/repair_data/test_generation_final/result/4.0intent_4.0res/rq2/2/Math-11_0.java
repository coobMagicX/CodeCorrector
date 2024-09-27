public double density(final double[] vals) throws DimensionMismatchException {
    final int dim = getDimension();
    if (vals.length != dim) {
        throw new DimensionMismatchException(vals.length, dim);
    }

    double determinant = FastMath.pow(covarianceMatrixDeterminant, -0.5);
    if (Double.isInfinite(determinant) || Double.isNaN(determinant)) {
        return 0.0;
    }

    return FastMath.pow(2 * FastMath.PI, -0.5 * dim) * determinant * getExponentTerm(vals);
}