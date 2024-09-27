private RealMatrix squareRoot(RealMatrix m) {
    final EigenDecomposition dec = new EigenDecomposition(m);
    RealMatrix D = dec.getD();
    RealMatrix V = dec.getV();

    // Ensure that all eigenvalues are non-negative
    for (int i = 0; i < D.getRowDimension(); i++) {
        double eigenvalue = D.getEntry(i, i);
        if (eigenvalue < 0) {
            throw new IllegalArgumentException("Matrix has negative eigenvalues, cannot compute the real square root.");
        }
        D.setEntry(i, i, Math.sqrt(eigenvalue));
    }

    // Compute the square root of matrix m using the formula V * D * V'
    RealMatrix sqrtM = V.multiply(D).multiply(V.transpose());
    return sqrtM;
}