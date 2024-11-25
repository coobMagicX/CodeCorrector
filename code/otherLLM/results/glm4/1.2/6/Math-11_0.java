public double density(final double[] vals) throws DimensionMismatchException {
    final int dim = getDimension();
    if (vals.length != dim) {
        throw new DimensionMismatchException("The number of values does not match the dimension.", dim);
    }

    double exponentTerm = getExponentTerm(vals);
    
    // Check if the exponent term is negative, which would imply a value outside the support
    if (exponentTerm < 0) {
        return 0.0; // Return zero density for values outside the support
    }

    return FastMath.pow(2 * FastMath.PI, -dim / 2) *
           FastMath.pow(covarianceMatrixDeterminant, -0.5) *
           exponentTerm;
}