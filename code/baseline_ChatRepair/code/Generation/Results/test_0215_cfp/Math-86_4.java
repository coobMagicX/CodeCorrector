public CholeskyDecompositionImpl(final RealMatrix matrix,
                                 final double relativeSymmetryThreshold,
                                 final double absolutePositivityThreshold)
    throws NonSquareMatrixException,
           NotSymmetricMatrixException, NotPositiveDefiniteMatrixException {

    if (!matrix.isSquare()) {
        throw new NonSquareMatrixException(matrix.getRowDimension(),
                                           matrix.getColumnDimension());
    }

    final int order = matrix.getRowDimension();
    lTData = matrix.getData();
    cachedL = null;
    cachedLT = null;

    
    for (int i = 0; i < order; ++i) {
        for (int j = i + 1; j < order; ++j) {
            double lIJ = lTData[i][j];
            double lJI = lTData[j][i];
            double maxDelta = relativeSymmetryThreshold * Math.max(Math.abs(lIJ), Math.abs(lJI));
            if (Math.abs(lIJ - lJI) > maxDelta) {
                throw new NotSymmetricMatrixException();
            }
            lTData[j][i] = 0; 
        }
    }

    
    for (int i = 0; i < order; ++i) {
        if (lTData[i][i] < absolutePositivityThreshold) {
            throw new NotPositiveDefiniteMatrixException("Matrix is not positive definite.");
        }

        lTData[i][i] = Math.sqrt(lTData[i][i]);
        if (Double.isNaN(lTData[i][i])) {
            throw new NotPositiveDefiniteMatrixException("Resulting diagonal element is not a number, indicating matrix is not positive definite.");
        }

        double inverseDiagonal = 1.0 / lTData[i][i];

        for (int k = i + 1; k < order; ++k) {
            lTData[i][k] *= inverseDiagonal;
            for (int j = k; j < order; ++j) {
                lTData[k][j] -= lTData[i][k] * lTData[i][j];
            }
        }
    }
}
