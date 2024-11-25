private RealMatrix squareRoot(RealMatrix m) {
    if (m == null || m.getColumnDimension() != 1) {
        throw new IllegalArgumentException("Input matrix must be a column vector");
    }

    final EigenDecomposition dec = new EigenDecomposition(m);
    dec.setTolerance(1e-12); // Regularization to address numerical stability issues
    return dec.getSquareRoot();
}