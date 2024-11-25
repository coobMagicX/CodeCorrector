public double density(final double[] vals) throws DimensionMismatchException {
    final int dim = getDimension();
    if (vals.length != dim) {
        throw new DimensionMismatchException("Number of input values (" + vals.length + ") does not match the dimension (" + dim + ")");
    }

    // Check if vals is an array with a single element, which means we need to handle univariate case
    if (dim == 1 && vals.length > 0) {
        return densityForUnivariate(vals[0]);
    } else {
        double exponentTerm = getExponentTerm(vals);
        return FastMath.pow(2 * FastMath.PI, -dim / 2) *
            FastMath.pow(covarianceMatrixDeterminant, -0.5) *
            exponentTerm;
    }
}

private double densityForUnivariate(final double value) {
    final double mean = getMeans()[0]; // Assuming the first element is the mean for a univariate case
    final double covariance = getCovariances()[0][0]; // Assuming the first element is the variance for a univariate case

    return FastMath.pow(2 * FastMath.PI, -0.5) *
        FastMath.pow(covariance, -0.5) *
        FastMath.exp(-0.5 * Math.pow(value - mean, 2) / covariance);
}

private double getExponentTerm(final double[] values) {
    final double[] centered = new double[values.length];
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