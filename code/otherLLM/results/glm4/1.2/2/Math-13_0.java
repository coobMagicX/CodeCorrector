private RealMatrix squareRoot(RealMatrix m) {
    final EigenDecomposition dec = new EigenDecomposition(m);
    // Ensure that the matrix is positive semi-definite to avoid numerical instability
    if (dec.isPositiveSemidefinite()) {
        return dec.getSquareRoot();
    } else {
        throw new IllegalArgumentException("The matrix is not positive semi-definite.");
    }
}