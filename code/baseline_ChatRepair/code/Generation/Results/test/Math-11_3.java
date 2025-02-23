public double density(final double[] vals) throws DimensionMismatchException {
    final int dim = getDimension();
    if (vals.length != dim) {
        throw new DimensionMismatchException(vals.length, dim);
    }

    
    double piTerm = FastMath.pow(2.0 * FastMath.PI, -0.5 * dim);
    double detTerm = FastMath.pow(covarianceMatrixDeterminant, -0.5);
    
    return piTerm * detTerm * getExponent