private RealMatrix squareRoot(RealMatrix m) {
    if (m == null) {
        throw new NullPointerException("Input matrix cannot be null.");
    }
    EigenDecomposition dec = new EigenDecomposition(m);
    RealMatrix squareRootMatrix = dec.getSquareRootMatrix();
    return squareRootMatrix;
}