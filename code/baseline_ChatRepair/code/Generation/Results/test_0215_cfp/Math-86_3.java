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
        final double[] ltI = lTData[i];

        
        if (ltI[i] <= absolutePositivityThreshold) {
            throw new NotPositiveDefiniteMatrixException();
        }

        ltI[i] = Math.sqrt(ltI[i]);
        if (Double.isNaN(ltI[i]) || ltI[i] <= absolutePositivityThreshold) {
            throw new NotPositiveDefiniteMatrixException();
        }

        final double inverse = 1.0 / ltI[i];

        for (int j = i + 1; j < order; ++j) {
            ltI[j] *= inverse;
        }

        
        for (int k = i + 1; k < order; ++k) {
            final double[] ltK = lTData[k];
            for (int j = k; j < order; ++j) {
                ltK[j] -= ltI[k] * ltI[j];
            }
        }
    }
}
