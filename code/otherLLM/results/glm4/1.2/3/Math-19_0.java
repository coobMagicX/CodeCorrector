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

    // If all bounds are infinite, set boundaries to null.
    if (!hasFiniteBounds) {
        boundaries = null;
    } else {
        // Convert API to internal handling of boundaries.
        boundaries = new double[2][];
        boundaries[0] = lB;
        boundaries[1] = uB;

        // Check for potential overflow during normalization before proceeding.
        if (isNormalizationOverflowRisk(init, lB, uB)) {
            throw new MathUnsupportedOperationException("Normalization will overflow.");
        }
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

private boolean isNormalizationOverflowRisk(double[] init, double[] lB, double[] uB) {
    // Example implementation: replace this with the actual check for overflow risk
    for (int i = 0; i < init.length; i++) {
        if ((init[i] - lB[i]) / (uB[i] - lB[i]) > Double.MAX_VALUE ||
            (init[i] - lB[i]) / (uB[i] - lB[i]) < -Double.MAX_VALUE) {
            return true;
        }
    }
    return false;
}