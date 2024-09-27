private void checkParameters() {
    final double[] init = getStartPoint();
    final double[] lB = getLowerBound();
    final double[] uB = getUpperBound();

    // Checks whether there is at least one finite bound value.
    boolean hasFiniteBounds = false;
    for (int i = 0; i < lB.length; i++) {
        if (!Double.isInfinite(lB[i]) || !Double.isInfinite(uB[i])) {
            hasFiniteBounds = true;
            break;
        }
    }

    if (hasFiniteBounds) {
        // Checks whether all bounds are finite, as mixed cases are not supported.
        boolean hasAnyInfiniteBound = false;
        for (int i = 0; i < lB.length; i++) {
            if (Double.isInfinite(lB[i]) || Double.isInfinite(uB[i])) {
                hasAnyInfiniteBound = true;
                break;
            }
        }

        if (hasAnyInfiniteBound) {
            // If there is at least one finite bound, none can be infinite,
            // because mixed cases are not supported by the current code.
            throw new MathUnsupportedOperationException("Mixed finite and infinite bounds are not supported.");
        }

        // Convert API to internal handling of boundaries.
        boundaries = new double[2][];
        boundaries[0] = lB;
        boundaries[1] = uB;
    } else {
        // No finite bounds exist, set boundaries to null.
        boundaries = null;
    }

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