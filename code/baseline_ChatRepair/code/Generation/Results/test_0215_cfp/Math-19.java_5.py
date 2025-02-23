private void checkParameters() {
    final double[] init = getStartPoint();
    final double[] lB = getLowerBound();
    final double[] uB = getUpperBound();
    
    // Ensure that the number of lower and upper bounds match the number of initial points
    if (lB.length != uB.length || init.length != lB.length) {
        throw new DimensionMismatchException(init.length, lB.length);
    }

    // Check consistency of bounds - ensuring no mix of finite and infinite bounds.
    boolean hasFinite = false;
    boolean hasInfinite = false;
    for (int i = 0; i < lB.length; i++) {
        if (!Double.isInfinite(lB[i]) && !Double.isInfinite(uB[i])) {
            hasFinite = true;
        }
        if (Double.isInfinite(lB[i]) || Double.isInfinite(uB[i])) {
            hasInfinite = true;
        }
    }

    if (hasFinite && hasInfinite) {
        throw new IllegalStateException("Invalid mix of finite and infinite bounds.");
    }

    boundaries = hasFinite ? new double[][]{lB, uB} : null;

    if (inputSigma != null) {
        if (inputSigma.length != init.length) {
            throw new DimensionMismatchException(inputSigma.length, init.length);
        }

        for (int i = 0; i < init.length; i++) {
            if (inputSigma[i] <= 0) {
                throw new NotPositiveException(inputSigma[i]);
            }
            if (boundaries != null && inputSigma[i] > (boundaries[1][i] - boundaries[0][i])) {
                throw new OutOfRangeException(inputSigma[i], 0, boundaries[1][i] - boundaries[0][i]);
            }
        }
    }
}
