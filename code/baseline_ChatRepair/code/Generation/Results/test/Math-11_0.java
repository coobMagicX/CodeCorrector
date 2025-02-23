public double density(final double[] vals) throws DimensionMismatchException {
    final int dim = getDimension();
    if (vals.length != dim) {
        throw new DimensionMismatchException(vals.length, dim);
    }

    
    double normalizationFactor = Math.pow(2 * Math.PI, -((double) dim) / 2.0);
    double detPower = Math.pow(covarianceMatrixDeterminant, -0.5);
    double exponentTerm = getExponentTerm(vals);

    return normalizationFactor