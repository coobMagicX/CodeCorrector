private RealMatrix squareRoot(RealMatrix m) {
    final EigenDecomposition dec = new EigenDecomposition(m);
    RealMatrix squareRoot = dec.getSquareRoot();
    return squareRoot;
}