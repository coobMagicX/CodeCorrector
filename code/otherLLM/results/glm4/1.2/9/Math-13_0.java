private RealMatrix squareRoot(RealMatrix m) {
    if (m == null || m.isSquare() == false) {
        throw new IllegalArgumentException("Matrix must be square and non-null");
    }
    
    final EigenDecomposition dec = new EigenDecomposition(m);
    return dec.getV().multiply(dec.getD().sqrt()).multiply(dec.getV().transpose());
}