public double density(final double[] vals) throws DimensionMismatchException {
    final int dim = getDimension();
    
    // Correct dimension handling for single-variable cases
    if (dim == 1) {
        return FastMath.exp(-vals[0] * vals[0] / covarianceMatrixDeterminant) /
                FastMath.sqrt(covarianceMatrixDeterminant);
    }
    
    if (vals.length != dim) {
        throw new DimensionMismatchException(vals.length, dim);
    }

    final double determinant = getExponentTerm(vals);

    return FastMath.pow(2 * FastMath.PI, -dim / 2) *
        FastMath.pow(determinant, -0.5) *
        determinant;
}