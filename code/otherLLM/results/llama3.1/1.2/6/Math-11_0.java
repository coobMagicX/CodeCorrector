public double density(final double[] vals) throws DimensionMismatchException {
    final int dim = getDimension();
    
    // Check if the input array has only one element (univariate)
    if (dim == 1 && vals.length == 1) {
        // If univariate, calculate the density as for a multivariate distribution with identity covariance matrix
        return FastMath.pow(2 * FastMath.PI, -dim / 2) *
            FastMath.pow(1, -0.5); 
    }
    
    if (vals.length != dim) {
        throw new DimensionMismatchException(vals.length, dim);
    }

    return FastMath.pow(2 * FastMath.PI, -dim / 2) *
        FastMath.pow(covarianceMatrixDeterminant(), -0.5) *
        getExponentTerm(vals);
}