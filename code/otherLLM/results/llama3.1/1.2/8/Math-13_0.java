private RealMatrix squareRoot(RealMatrix m) {
    final EigenDecomposition dec = new EigenDecomposition(m);
    // Add a check to ensure the matrix is positive semi-definite before taking the square root
    if (!dec.getActualM().isPositiveSemidefinite()) {
        throw new NonPositiveDefiniteMatrixException("Matrix is not positive semi-definite");
    }
    return dec.getSquareRoot();
}