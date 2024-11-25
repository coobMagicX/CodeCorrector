private RealMatrix squareRoot(RealMatrix m) {
    final EigenDecomposition dec = new EigenDecomposition(m);

    // Ensure the matrix is positive definite for a valid square root
    if (!dec.isPositiveDefinite()) {
        throw new IllegalArgumentException("Matrix must be positive definite to compute its square root.");
    }

    return dec.getSquareRoot();
}