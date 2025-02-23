private void checkParameters() {
    final double[] init = getStartPoint();
    final double[] lB = getLowerBound();
    final double[] uB = getUpperBound();

    boolean hasFiniteBounds = false;
    boolean hasInfiniteBounds = false;
    
    for (int i = 0; i < lB.length; i++) {
        if (!Double.isInfinite(lB[i]) || !Double.isInfinite(uB[i])) {
            hasFiniteBounds = true;
        }
        if (Double.isInfinite(lB[i]) || Double.isInfinite(uB[i])) {
            hasInfiniteBounds = true;
        }
    }

    if (hasInfiniteBounds && hasFiniteBounds) {
        // If there are both finite and infinite bounds, an exception is thrown.
        throw new MathUnsupportedOperationException("Mixed finite and infinite bounds are not supported.");
    }
    
    if (hasFiniteBounds) {
        // Convert API to internal handling of boundaries if there are finite boundaries.
        boundaries = new double[2][];
        boundaries[0] = lB;
        boundaries[1] = uB;
    } else {
        // All bounds are infinite, thus no boundaries required.
        boundaries = null;
    }

    if (inputSigma != null) {
        if (inputSigma.length != init.length) {
            throw new DimensionMismatchException(inputSigma.length, init.length);
        }
        for (int i = 0; i < init.length; i++) {
            if (inputSigma[i] < 0) {
                throw new NotPositiveException(inputSigma[i]);
            }
            if (boundaries != null) {
                double range = boundaries[1][i] - boundaries[0][i];
                if (inputSigma[i] > range) {
                    throw new OutOfRangeException(inputSigma[i], 0, range);
                }
            }
        }
    }
}
