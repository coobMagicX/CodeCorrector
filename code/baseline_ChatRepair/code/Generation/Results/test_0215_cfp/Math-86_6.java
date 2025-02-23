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
            final double lIJ = lTData[i][j];
            final double lJI = lTData[j][i];
            final double maxDelta = relativeSymmetryThreshold * Math.max(Math.abs(lIJ), Math.abs(lJI));
            if (Math.abs(lIJ - lJI) > maxDelta) {
                throw new NotSymmetricMatrixException();
            }
            lTData[j][i] = 0;  
        }
    }

    
    for (int i = 0; i < order; ++i) {
        if (lTData[i][i] < absolutePositivityThreshold) {
            throw new NotPositiveDefiniteMatrixException();
        }

        lTData[i][i] = Math.sqrt(lTData[i][i]);
        if (Double.isNaN(lTData[i][i])) {
            throw new NotPositiveDefiniteMatrixException();
        }

        final double inverse = 1.0 / lTData[i][i];

        for (int q = i + 1; q < order; ++q) {
            lTData[i][q] *= inverse;  
            for (int p = q; p < order; ++p) {
                lTData[p][q] -= lTData[i][q] * lTData[i][p];  
            }
        }
    }
}
