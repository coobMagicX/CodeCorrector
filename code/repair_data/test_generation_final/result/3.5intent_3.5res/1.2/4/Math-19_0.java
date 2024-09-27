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
            if (checkNormalizationOverflow()) {
                throw new MathUnsupportedOperationException();
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
                double maxSigma = boundaries[1][i] - boundaries[0][i];
                if (maxSigma <= 0 || inputSigma[i] > maxSigma) {
                    throw new OutOfRangeException(inputSigma[i], 0, maxSigma);
                }
            }
        }
    }
}

private boolean checkNormalizationOverflow() {
    final double[] lB = boundaries[0];
    final double[] uB = boundaries[1];

    for (int i = 0; i < lB.length; i++) {
        if (Double.isInfinite(lB[i]) || Double.isInfinite(uB[i])) {
            continue;
        }

        double diff = uB[i] - lB[i];
        if (diff > MAX_NORMALIZATION_DIFF) {
            return true;
        }
    }

    return false;
}

private double[] repair(final double[] x) {
    double[] repaired = new double[x.length];
    for (int i = 0; i < x.length; i++) {
        if (x[i] < 0) {
            repaired[i] = 0;
        } else if (x[i] > 1.0) {
            repaired[i] = 1.0;
        } else {
            repaired[i] = x[i];
        }
    }
    return repaired;
}