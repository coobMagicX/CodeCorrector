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
    for (int i = 0; i < lB.length; i++) {
        if (Double.isInfinite(lB[i]) ||
            Double.isInfinite(uB[i])) {
            hasInfiniteBounds = true;
            break;
        }
    }

    // If there is at least one finite bound, none can be infinite,
    // because mixed cases are not supported by the current code.
    if (hasFiniteBounds && hasInfiniteBounds) {
        throw new MathUnsupportedOperationException();
    } else if (!hasFiniteBounds && !hasInfiniteBounds) {
        // If both have no finite or infinite bounds, boundaries is set to null
        boundaries = null;
    } else {
        // Convert API to internal handling of boundaries.
        boundaries = new double[2][];
        boundaries[0] = lB;
        boundaries[1] = uB;

        // Abort early if the normalization will overflow (cf. "encode" method).
        long rangeOverflow = Long.MIN_VALUE + 100 * init.length;
        for (int i = 0; i < lB.length; i++) {
            long lowerBound = Math.max(Long.MIN_VALUE, (long)lB[i]);
            long upperBound = Math.min(Long.MAX_VALUE, (long)uB[i]);

            if (lowerBound > upperBound) {
                throw new MathOverflowException();
            }

            rangeOverflow += lowerBound + upperBound;
        }
    }

    // Checks whether inputSigma has correct length.
    if (inputSigma != null && inputSigma.length != init.length) {
        throw new DimensionMismatchException(inputSigma.length, init.length);
    }
    
    for (int i = 0; i < init.length; i++) {
        if (inputSigma != null && inputSigma[i] < 0) {
            throw new NotPositiveException(inputSigma[i]);
        }
        
        if (boundaries != null) {
            if (inputSigma != null && inputSigma[i] > boundaries[1][i] - boundaries[0][i]) {
                throw new OutOfRangeException(inputSigma[i], 0, boundaries[1][i] - boundaries[0][i]);
            }
        }
    }
}