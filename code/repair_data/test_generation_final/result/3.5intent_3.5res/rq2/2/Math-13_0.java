private RealMatrix squareRoot(RealMatrix m) {
    final EigenDecomposition dec = new EigenDecomposition(m);
    final RealMatrix squareRoot = dec.getSquareRoot();
    
    // Check if the square root matrix is null
    if (squareRoot == null) {
        throw new IllegalArgumentException("Cannot calculate square root of matrix");
    }
    
    return squareRoot;
}