public double density(final double[] vals) throws DimensionMismatchException {
    final int dim = getDimension();
    if (vals.length != dim) {
        throw new DimensionMismatchException(vals.length, dim);
    }

    // Ensure that vals is an array of one value for univariate distribution comparison
    if (dim == 1 && vals.length > 1) {
        throw new IllegalArgumentException("Univariate distribution expects a single value");
    }
    
    double density = FastMath.pow(2 * FastMath.PI, -0.5) *
                      FastMath.pow(covarianceMatrixDeterminant, -0.5);

    // Calculate the exponent term for univariate case
    if (dim == 1) {
        final double centeredVal = vals[0] - getMeans()[0];
        density *= FastMath.exp(-0.5 * Math.pow(centeredVal, 2));
    } else {
        // For multivariate case, use the existing method to calculate the exponent term
        final double[] centered = new double[vals.length];
        for (int i = 0; i < vals.length; i++) {
            centered[i] = vals[i] - getMeans()[i];
        }
        final double[] preMultiplied = covarianceMatrixInverse.preMultiply(centered);
        double sum = 0;
        for (int i = 0; i < preMultiplied.length; i++) {
            sum += preMultiplied[i] * centered[i];
        }
        density *= FastMath.exp(-0.5 * sum);
    }

    return density;
}