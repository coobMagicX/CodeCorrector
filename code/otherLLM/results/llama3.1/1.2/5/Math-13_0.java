private RealMatrix squareRoot(RealMatrix m) {
    final EigenDecomposition dec = new EigenDecomposition(m);
    // Validate EigenDecomposition output by checking if the matrix is positive semi-definite
    if (!dec.isPositiveDefinite()) {
        throw new NonPositiveDefiniteMatrixException();
    }
    return dec.getSquareRoot();
}