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

    // Correct infinite bound checking
    boolean hasInfiniteLowerBound = false;
    for (int i = 0; i < lB.length; i++) {
        if (Double.isInfinite(lB[i])) {
            hasInfiniteLowerBound = true;
            break;
        }
    }
    
    boolean hasInfiniteUpperBound = false;
    for (int i = 0; i < uB.length; i++) {
        if (Double.isInfinite(uB[i])) {
            hasInfiniteUpperBound = true;
            break;
        }
    }

    // Checks whether there is at least one infinite bound value.
    boolean hasInfiniteBounds = false;
    if (hasFiniteBounds) {
        if (hasInfiniteLowerBound && hasInfiniteUpperBound) {
            throw new MathUnsupportedOperationException();
        } else {
            // Convert API to internal handling of boundaries.
            boundaries = new double[2][];
            boundaries[0] = lB;
            boundaries[1] = uB;

            // Abort early if the normalization will overflow (cf. "encode" method).
            long diff = 0L;
            for (int i = 0; i < init.length; i++) {
                double lower = lB[i];
                double upper = uB[i];
                if (!Double.isInfinite(lower) && !Double.isInfinite(upper)) {
                    diff += Long.signum((long) (upper - lower));
                }
            }
            if ((diff > Integer.MAX_VALUE || diff < Integer.MIN_VALUE) &&
                !(init.length == 1 && Math.abs(diff) <= Integer.MAX_VALUE)) {
                throw new OverflowException();
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
                double diff = uB[i] - lB[i];
                if (diff == Double.POSITIVE_INFINITY || diff == Double.NEGATIVE_INFINITY) {
                    continue;
                } else if (inputSigma[i] > diff) {
                    throw new OutOfRangeException(inputSigma[i], 0, diff);
                }
            }
        }
    }
}