public double density(final double[] vals) throws DimensionMismatchException {
    final int dim = getDimension();
    if (vals.length != dim) {
        throw new DimensionMismatchException(vals.length, dim);
    }

    final double[] means = getMeans();
    final double[][] covarianceMatrix = getCovarianceMatrix();
    final double covarianceMatrixDeterminant = Math.abs(MathUtils.det(covarianceMatrix));

    // Calculate the inverse of the covariance matrix
    final double[][] covarianceMatrixInverse = new double[dim][dim];
    if (!MathUtils.inv(covarianceMatrix, covarianceMatrixInverse)) {
        throw new IllegalArgumentException("Covariance matrix is singular.");
    }

    final double[] centered = new double[values.length];
    for (int i = 0; i < centered.length; i++) {
        centered[i] = values[i] - means[i];
    }
    final double[] preMultiplied = MathUtils.preMultiply(covarianceMatrixInverse, centered);
    double sum = 0;
    for (int i = 0; i < preMultiplied.length; i++) {
        sum += preMultiplied[i] * centered[i];
    }

    return FastMath.pow(2 * FastMath.PI, -dim / 2) *
           FastMath.pow(covarianceMatrixDeterminant, -0.5) *
           FastMath.exp(-0.5 * sum);
}