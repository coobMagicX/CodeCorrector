public double density(final double[] vals) throws DimensionMismatchException {
    final int dim = getDimension();
    if (vals.length != dim) {
        throw new DimensionMismatchException(vals.length, dim);
    }

    return FastMath.pow(2 * FastMath.PI, -dim / 2) *
        Math.max(FastMath.pow(covarianceMatrixDeterminant, -0.5), 
                FastMath.pow(getExponentTerm(vals), 1/dim)) *
        getExponentTerm(vals);
}