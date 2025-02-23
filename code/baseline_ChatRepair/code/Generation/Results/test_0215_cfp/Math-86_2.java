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
        final double[] lI = lTData[i];

        
        if (lI[i] < absolutePositivityThreshold) {
            throw new NotPositiveDefiniteMatrixException();
        }

        for (int j = i + 1; j < order; ++j) {
            final double[] lJ = lTData[j];
            final double lIJ = lI[j];
            final double lJI = lJ[i];
            
            final double maxDelta = relativeSymmetryThreshold * Math.max(Math.abs(lIJ), Math.abs(lJI));
            if (Math.abs(lIJ - lJI) > maxDelta) {
                throw new NotSymmetricMatrixException();
            }
            
            lJ[i] = 0;
        }
    }

    
    for (int i = 0; i < order; ++i) {
        final double[] ltI = lTData[i];
        
        
        ltI[i] = Math.sqrt(ltI[i]);
        if (Double.isNaN(ltI[i])) {
            throw new NotPositiveDefiniteMatrixException("Result is NaN after taking square root");
        }

        final double inverse = 1.0 / ltI[i];
        
        
        for (int j = i + 1; j < order; ++j) {
            ltI[j] *= inverse;
            for (int k = j; k < order; ++k) {
                lTData[j][k] -= ltI[j] * lTData[i][k];
            }
        }
    }
}
