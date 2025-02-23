public CholeskyDecompositionImpl(final RealMatrix matrix,
                                 final double relativeSymmetryThreshold,
                                 final double absolutePositivityThreshold)
    throws NonSquareMatrixException,
           NotSymmetricMatrixException, NotPositiveDefiniteMatrixException {

    if (!matrix.isSquare()) {
        throw new NonSquareMatrixException(matrix.getRowDimension(), matrix.getColumnDimension());
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
        }
    }

    
    for (int i = 0; i < order; ++i) {
        for (int j = 0; j <= i; ++j) {
            double sum = lTData[i][j];
            for (int k = 0; k < j; k++) {
                sum -= lTData[i][k] * lTData[j][k];
            }
            if (i == j) {
                if (sum <= absolutePositivityThreshold) {
                    throw new NotPositiveDefiniteMatrixException();
                }
                lTData[i][i] = Math.sqrt(sum);
            } else {
                lTData[i][j] = sum / lTData[j][j];
            }
        }
    }

    
    for (int i = 0; i < order; ++i) {
        for (int j = i + 1; j < order; ++j) {
            lTData[i][j] = 0.0;
        }
    }
}
