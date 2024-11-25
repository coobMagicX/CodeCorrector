public double density(final double[] vals) throws DimensionMismatchException {
    final int dim = getDimension();
    if (vals.length != 1 || dim > 1) { // Changed to check for univariate case (length == 1)
        throw new DimensionMismatchException(vals.length, dim);
    }

    double[] flatArray = new double[1]; // Create a flat array of size 1
    System.arraycopy(vals, 0, flatArray, 0, vals.length); // Copy the single-element input to the flat array

    return FastMath.pow(2 * FastMath.PI, -dim / 2) *
        FastMath.pow(covarianceMatrixDeterminant, -0.5) *
        getExponentTerm(flatArray);
}