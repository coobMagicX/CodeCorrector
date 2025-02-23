public double density(final double[] vals) throws DimensionMismatchException {
    final int dim = getDimension();
    if (vals.length != dim) {
        throw new DimensionMismatchException(vals.length, dim);
    }

    
    double negativeHalfDim = -((double) dim) / 2.0;
    
    return FastMath.pow(2 *