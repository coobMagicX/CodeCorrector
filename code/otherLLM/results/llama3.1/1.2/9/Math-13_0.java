private RealMatrix squareRoot(RealMatrix m) {
    // Check if input matrix is singular or ill-conditioned
    if (m.getDeterminant().equals(FastMath.ZERO)) {
        throw new SingularMatrixException("Input matrix is singular.");
    }

    final EigenDecomposition dec = new EigenDecomposition(m);
    
    // Re-calculate square root for each polynomial term separately
    RealMatrix squareRoots = AlgebraicUtil.createRealMatrix(rows, 1);
    for (int i = 0; i < rows; i++) {
        double sqrtValue = getRMS() * m.getEntry(i, i);
        squareRoots.setEntry(i, 0, sqrtValue);
    }
    
    return squareRoots;
}