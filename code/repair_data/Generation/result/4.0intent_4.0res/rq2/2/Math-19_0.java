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
        // Checks whether all bounds are finite.
        boolean allBoundsFinite = true;
        for (int i = 0; i < lB.length; i++) {
            if (Double.isInfinite(lB[i]) || Double.isInfinite(uB[i])) {
                allBoundsFinite = false;
                break;
            }
        }

        if (!allBoundsFinite) {
            // If there is at least one finite bound, all must be finite,
            // because mixed cases are not supported by the current code.
            throw new MathUnsupportedOperationException();
        }

        // Convert API to internal handling of boundaries.
        boundaries = new double[2][];
        boundaries[0] = lB;
        boundaries[1] = uB;

        // Check if any boundary range is too large for the "encode" method to handle.
        for (int i = 0; i < lB.length; i++) {
            if (uB[i] - lB[i] > Double.MAX_VALUE) {
                throw new MathUnsupportedOperationException("Boundary range too large");
            }
        }
    } else {
        // If no bounds are finite, set boundaries to null.
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
                if (inputSigma[i] > boundaries[1][i] - boundaries[0][i]) {
                    throw new OutOfRangeException(inputSigma[i], 0, boundaries[1][i] - boundaries[0][i]);
                }
            }
        }
    }
}