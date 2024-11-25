private double getExponentTerm(final double[] values) {
    final int dim = getDimension();
    if (values.length != dim) {
        throw new DimensionMismatchException(values.length, dim);
    }

    final double[] centered = new double[values.length];
    for (int i = 0; i < centered.length; i++) {
        centered[i] = values[i] - getMeans()[i];
    }
    
    // Ensure the correct matrix is being used
    final Matrix covarianceMatrixInverse = this.isCovarianceMatrixSingular()
            ? MatrixSolver.getInverse(covarianceMatrix)
            : new Matrix(covarianceMatrix);

    final double[] preMultiplied = covarianceMatrixInverse.preMultiply(centered);
    double sum = 0;
    for (int i = 0; i < preMultiplied.length; i++) {
        sum += preMultiplied[i] * centered[i];
    }
    return FastMath.exp(-0.5 * sum);
}

public double density(final double[] vals) throws DimensionMismatchException {
    final int dim = getDimension();
    if (vals.length != dim) {
        throw new DimensionMismatchException(vals.length, dim);
    }

    // Ensure the correct matrix is being used
    final Matrix covarianceMatrixInverse = this.isCovarianceMatrixSingular()
            ? MatrixSolver.getInverse(covarianceMatrix)
            : new Matrix(covarianceMatrix);

    double exponentTerm = getExponentTerm(vals);
    return FastMath.pow(2 * FastMath.PI, -dim / 2) *
           FastMath.pow(covarianceMatrixDeterminant(), -0.5) *
           exponentTerm;
}