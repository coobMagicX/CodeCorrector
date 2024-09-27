private RealMatrix squareRoot(RealMatrix m) {
    final EigenDecomposition dec = new EigenDecomposition(m);
    RealMatrix D = dec.getD();
    RealMatrix V = dec.getV();

    // Ensure matrix D only contains positive or zero values
    for (int i = 0; i < D.getRowDimension(); i++) {
        if (D.getEntry(i, i) < 0) {
            D.setEntry(i, i, 0);
        } else {
            D.setEntry(i, i, Math.sqrt(D.getEntry(i, i)));
        }
    }

    // Calculate the square root of matrix 'm' using V * D * V'
    return V.multiply(D).multiply(V.transpose());
}