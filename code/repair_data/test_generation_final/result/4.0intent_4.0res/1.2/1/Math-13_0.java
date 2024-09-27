private RealMatrix squareRoot(RealMatrix m) {
    if (m == null || m.getColumnDimension() != m.getRowDimension()) {
        throw new NonSquareMatrixException(m.getRowDimension(), m.getColumnDimension());
    }

    final EigenDecomposition dec = new EigenDecomposition(m);
    RealMatrix sqrtMatrix = dec.getSquareRoot();

    // Verify if the resulting matrix is valid
    if (sqrtMatrix == null || sqrtMatrix.getColumnDimension() != sqrtMatrix.getRowDimension()) {
        throw new MathRuntimeException(LocalizedFormats.NON_POSITIVE_DEFINITE_MATRIX);
    }

    return sqrtMatrix;
}