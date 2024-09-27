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

    // Checks whether all bounds are infinite. This variable is initialized to true.
    boolean allInfiniteBounds = true;
    for (int i = 0; i < lB.length; i++) {
        if (!Double.isInfinite(lB[i]) || !Double.isInfinite(uB[i])) {
            allInfiniteBounds = false;
            break;
        }
    }

    // Handling of bounds based on their finiteness
    if (!allInfiniteBounds) {
        // Convert API to internal handling of boundaries when not all bounds are infinite.
        boundaries = new double[2][];
        boundaries[0] = lB;
        boundaries[1] = uB;
    } else {
        // Set boundaries to null when all bounds are infinite.
        boundaries = null;
    }

    // Validation for inputSigma related to init and boundaries
    if (inputSigma != null) {
        if (inputSigma.length != init.length) {
            throw new DimensionMismatchException(inputSigma.length, init.length);
        }
        for (int i = 0; i < init.length; i++) {
            if (inputSigma[i] <= 0) {
                throw new NotPositiveException(inputSigma[i]);
            }
            if (boundaries != null && (inputSigma[i] > boundaries[1][i] - boundaries[0][i])) {
                throw new OutOfRangeException(inputSigma[i], 0, boundaries[1][i] - boundaries[0][i]);
            }
        }
    }
}