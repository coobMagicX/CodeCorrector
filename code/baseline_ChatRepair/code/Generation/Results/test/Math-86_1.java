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
    lTData   = matrix.getData();
    cachedL  = null;
    cachedLT = null;

    
    for (int i = 0; i < order; ++i) {
        final double[] lI = lTData[i];

        if (lTData[i][i] <= 0 || Math.abs(lTData[i][i]) < absolutePositivityThreshold) {
            throw new NotPositiveDefiniteMatrixException();
        }

        for (int j = i + 1; j < order; ++j) {
            final double[] lJ = lTData[j];
            if (Math.abs(lI[j] - lJ[i]) > relativeSymmetryThreshold * Math.max(Math.abs(lI[j]), Math.abs(lJ[i]))) {
                throw new NotSymmetricMatrixException();
            }
            lJ[i] = 0;  
       }
    }

    
    for (int i = 0; i < order; ++i) {
        final double[] ltI = lTData[i];
        ltI[i] = Math.sqrt(ltI[i]);  
        if (Double.isNaN(ltI[i])) {
            throw new NotPositiveDefiniteMatrixException();  
        }

        final double inverse = 1.0 / ltI[i];

        for (int q = order - 1; q > i; --q) {
            ltI[q] *= inverse;
            final double[] ltQ = lTData[q];
            for (int p = q; p < order; ++p) {
                ltQ[p] -= ltI[q] * ltI[p];  
            }
        }
    }

}
