public double density(final double[] vals) throws DimensionMismatchException {
    final int dim = getDimension();
    if (vals.length != dim) {
        throw new DimensionMismatchException(vals.length, dim);
    }
    
    // Correct the calculation of the multiplier for the exponential term
    // by ensuring that the power `-dim / 2` is correctly computed as a floating-point division.
    return FastMath.pow(2 * FastMath.PI, -0.5 * dim) *
        FastMath.pow(covarianceMatrixDeterminant, -0.5) *
        getExponentTerm(vals);
}