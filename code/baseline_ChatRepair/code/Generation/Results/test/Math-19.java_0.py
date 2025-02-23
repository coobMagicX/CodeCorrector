private void checkParameters() {
    final double[] init = getStartPoint();
    final double[] lB = getLowerBound();
    final double[] uB = getUpperBound();

    if (lB.length != uB.length || init.length != lB.length) {
        throw new DimensionMismatchException(init.length, lB.length);
    }

    // Initialize the boundaries for functionality based on actual bounds provided.
    boundaries = new double[2][];
    boundaries[0] = lB;
    boundaries[1] = uB;

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

    // Check for mixed finite and infinite bounds which are unsupported
    if (hasFiniteBounds && hasInfiniteBounds) {
        throw new MathUnsupportedOperationException("Mixed finite and infinite bounds are not supported.");
    }

    // Validate inputSigma if provided.
    if (inputSigma != null) {
        if (inputSigma.length != init.length) {
            throw new DimensionMismatchException(inputSigma.length, init.length);
        }
        for (int i = 0; i < init.length; i++) {
            if (inputSigma[i] < 0) {
                throw new NotPositiveException(inputSigma[i]);
            }
            if (boundaries != null && !Double.isInfinite(boundaries[0][i]) && !Double.isInfinite(boundaries[1][i])) {
                double range = boundaries[1][i] - boundaries[0][i];
                if (inputSigma[i] > range) {
                    throw new OutOfRangeException(inputSigma[i], 0, range);
                }
            }
        }
    }
}
