private RealMatrix squareRoot(RealMatrix m) {
    final EigenDecomposition dec = new EigenDecomposition(m, Double.MIN_VALUE);
    RealMatrix squareRoot = dec.getSquareRoot();
    
    if (squareRoot == null) {
        throw new IllegalArgumentException("Square root of matrix is not defined");
    }
    
    return squareRoot;
}