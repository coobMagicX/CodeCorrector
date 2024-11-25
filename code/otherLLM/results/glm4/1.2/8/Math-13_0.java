private RealMatrix squareRoot(RealMatrix m) {
    // Check if the matrix is not null and has a valid size
    if (m == null || m.getRowDimension() != m.getColumnDimension()) {
        throw new IllegalArgumentException("The matrix must be non-null and square.");
    }
    
    final EigenDecomposition dec = new EigenDecomposition(m);
    // Ensure that the method getSquareRoot is available in the used library
    if (dec.getSquareRoot == null) {
        throw new UnsupportedOperationException("The getSquareRoot() method is not supported by this implementation of EigenDecomposition.");
    }

    return dec.getSquareRoot();
}