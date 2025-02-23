private void checkParameters() {
    final double[] init = getStartPoint();
    final double[] lB = getLowerBound();
    final double[] uB = getUpperBound();

    // Ensure that the number of lower and upper bounds match the number of initial points
    if (lB.length != uB.length || init.length != lB.length) {
        throw new DimensionMismatchException(init.length, lB.length);
    }

    // Check consistency of bounds - ensuring all are either finite or infinite properly.
    boolean finiteFound = false;
    boolean infiniteFound = false;
    for (int i = 0; i < lB.length; i++) {
        boolean lBInfinite = Double.isInfinite(lB[i]);
        boolean uBInfinite = Double.isInfinite(uB[i]);
        if (!lBInfinite && !uBInfinite) {
            finiteFound = true;
        }
        if (lBInfinite || uBInfinite) {
            infiniteFound = true;
        }
    }

    if (finiteFound && infiniteFound) {
        throw new MathIllegalStateException(); // Or another specific exception indicating mixed bounds scenario
    }

    boundaries = finiteFound ? new double[][]{lB, uB} : null;

    if (inputSigma != null) {
        if (inputSigma.length != init.length) {
            throw new DimensionMismatchException(inputSigma.length, init.length);
        }
        
        for (int i = 0; i < init.length; i++) {
            if (inputSigma[i] <= 0) {
                throw new NotPositiveException(inputSigma[i]);
            }
            if (boundaries != null && inputSigma[i] > boundaries[1][i] - boundaries[0][i]) {
                throw new OutOfRangeException(inputSigma[i], 0, boundaries[1][i] - boundaries[0][i]);
            }
        }
    }
}
