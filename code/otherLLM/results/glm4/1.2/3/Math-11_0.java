public double density(final double[] vals) throws DimensionMismatchException {
    final int dim = getDimension();
    if (vals.length != dim) {
        throw new DimensionMismatchException("Number of values provided does not match the dimension.", dim);
    }

    double exponentTerm = getExponentTerm(vals);

    // For a univariate distribution, the covariance matrix is just a scalar value
    if (dim == 1) {
        return FastMath.pow(2 * FastMath.PI, -0.5) *
               FastMath.pow(covarianceMatrixDeterminant, -0.5) *
               exponentTerm;
    } else { // For multivariate distribution
        return FastMath.pow(2 * FastMath.PI, -dim / 2) *
               FastMath.pow(covarianceMatrixDeterminant, -0.5) *
               exponentTerm;
    }
}

private double getExponentTerm(final double[] values) {
    final int dim = getDimension();
    final double[] centered = new double[dim];
    for (int i = 0; i < centered.length; i++) {
        centered[i] = values[i] - getMeans()[i];
    }
    final double[] preMultiplied = covarianceMatrixInverse.preMultiply(centered);
    double sum = 0;
    for (int i = 0; i < preMultiplied.length; i++) {
        sum += preMultiplied[i] * centered[i];
    }
    return FastMath.exp(-0.5 * sum);
}