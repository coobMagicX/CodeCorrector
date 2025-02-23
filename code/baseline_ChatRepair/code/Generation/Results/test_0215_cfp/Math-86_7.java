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
        double[] lI = lTData[i];
        for (int j = i + 1; j < order; ++j) {
            double lIJ = lI[j];
            double lJI = lTData[j][i];
            double maxDelta = relativeSymmetryThreshold * Math.max(Math.abs(lIJ), Math.abs(lJI));
            if (Math.abs(lIJ - lJI) > maxDelta) {
                throw new NotSymmetricMatrixException();
            }
            lTData[j][i] = 0;  
        }
    }

    
    for (int i = 0; i < order; ++i) {
        final double[] ltI = lTData[i];
        
        
        if (ltI[i] < absolutePositivityThreshold) {
            throw new NotPositiveDefiniteMatrixException();
        }

        ltI[i] = Math.sqrt(ltI[i]);
        if (ltI[i] == 0 || Double.isNaN(ltI[i])) {  
            throw new NotPositiveDefiniteMatrixException();
        }

        double inverse = 1.0 / ltI[i];

        for (int j = i + 1; j < order; ++j) {
            ltI[j] *= inverse;
        }
        
        for (int j = i + 1; j < order; ++j) {
            double[] ltJ = lTData[j];
            for (int k = j; k < order; ++k) {
                ltJ[k] -= ltI[j] * ltI[k];
            }
        }
    }
}
