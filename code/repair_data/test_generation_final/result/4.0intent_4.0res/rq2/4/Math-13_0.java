private RealMatrix squareRoot(RealMatrix m) {
    if (m == null) {
        throw new IllegalArgumentException("Input matrix cannot be null.");
    }
    if (!m.isSquare()) {
        throw new IllegalArgumentException("Matrix must be square.");
    }

    final EigenDecomposition dec = new EigenDecomposition(m);
    return dec.getSquareRoot();
}