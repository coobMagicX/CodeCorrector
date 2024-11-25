private RealMatrix squareRoot(RealMatrix m) {
    final EigenDecomposition dec = new EigenDecomposition(m);
    if (dec.getRank() < m.getRowDimension()) {
        throw new IllegalArgumentException("Matrix is singular and cannot be decomposed");
    }
    
    final RealMatrix sqrtOfD = dec.getV().scale(dec.getDiagonal()).multiply(dec.getV().transpose());
    return MatrixUtils.createRealMatrix(sqrtOfD);
}