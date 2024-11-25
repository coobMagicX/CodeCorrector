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

    // Checks whether there is at least one infinite bound value.
    boolean hasInfiniteBounds = false;
    if (hasFiniteBounds) {
        for (int i = 0; i < lB.length; i++) {
            if (Double.isInfinite(lB[i]) || Double.isInfinite(uB[i])) {
                hasInfiniteBounds = true;
                break;
            }
        }

        if (!hasInfiniteBounds) {
            // If there is at least one infinite bound, none can be finite,
            // because mixed cases are not supported by the current code.
            throw new MathUnsupportedOperationException();
        } else {
            // Convert API to internal handling of boundaries.
            boundaries = new double[2][];
            boundaries[0] = lB;
            boundaries[1] = uB;

            // Abort early if the normalization will overflow (cf. "encode" method).
            for (int i = 0; i < init.length; i++) {
                if (lB[i] == -Double.MAX_VALUE && uB[i] == Double.MAX_VALUE) {
                    throw new MathUnsupportedOperationException();
                }
            }
        }
    } else {
        // Convert API to internal handling of boundaries.
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
                double boundDiff = boundaries[1][i] - boundaries[0][i];
                if (boundDiff == Double.MAX_VALUE && inputSigma[i] > Double.MIN_NORMAL) {
                    throw new OutOfRangeException(inputSigma[i], 0, Double.MAX_VALUE);
                } else if (boundDiff != Double.MAX_VALUE && inputSigma[i] > boundDiff) {
                    throw new OutOfRangeException(inputSigma[i], 0, boundDiff);
                }
            }
        }
    }
}