private void checkParameters() {
    final double[] init = getStartPoint();
    final double[] lB = getLowerBound();
    final double[] uB = getUpperBound();

    // Checks whether there is at least one finite bound value.
    boolean hasFiniteBounds = false;
    for (int i = 0; i < lB.length; i++) {
        if (!Double.isInfinite(lB[i]) ||
            !Double.isInfinite(uB[i])) {
            hasFiniteBounds = true;
            break;
        }
    }
    // Checks whether there is at least one infinite bound value.
    boolean hasInfiniteBounds = false;
    if (hasFiniteBounds) {
        for (int i = 0; i < lB.length; i++) {
            if (Double.isInfinite(lB[i]) ||
                Double.isInfinite(uB[i])) {
                hasInfiniteBounds = true;
                break;
            }
        }

        if (hasInfiniteBounds) {
            // If there is at least one finite bound, none can be infinite,
            // because mixed cases are not supported by the current code.
            throw new MathUnsupportedOperationException();
        } else {
            // Convert API to internal handling of boundaries.
            boundaries = new double[2][];
            boundaries[0] = lB;
            boundaries[1] = uB;

            // Abort early if the normalization will overflow (cf. "encode" method).
            if (isNormalizationOverflow(init, lB, uB)) {
                throw new ArithmeticException("Normalization overflow");
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
            if (boundaries != null && isSigmaOutOfRange(inputSigma[i], boundaries[1][i] - boundaries[0][i])) {
                throw new OutOfRangeException(inputSigma[i], 0, boundaries[1][i] - boundaries[0][i]);
            }
        }
    }
}

private boolean isNormalizationOverflow(double[] init, double[] lB, double[] uB) {
    for (int i = 0; i < init.length; i++) {
        double diff = uB[i] - lB[i];
        if (Double.isInfinite(diff) || Double.isNaN(diff) || Double.isInfinite(init[i]) || Double.isNaN(init[i])) {
            return true;
        }
        double norm = (init[i] - lB[i]) / diff;
        if (Double.isInfinite(norm) || Double.isNaN(norm) || Double.isInfinite(diff) || Double.isNaN(diff)) {
            return true;
        }
    }
    return false;
}

private boolean isSigmaOutOfRange(double sigma, double range) {
    return sigma > range || Double.isNaN(sigma) || Double.isInfinite(sigma) || Double.isNaN(range) || Double.isInfinite(range);
}