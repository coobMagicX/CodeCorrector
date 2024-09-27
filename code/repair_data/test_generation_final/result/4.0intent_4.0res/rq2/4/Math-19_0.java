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
        // Initialize boundaries to handle finite bounds
        boundaries = new double[2][];
        boundaries[0] = lB;
        boundaries[1] = uB;

        for (int i = 0; i < lB.length; i++) {
            if (Double.isInfinite(lB[i]) || Double.isInfinite(uB[i])) {
                // If there is at least one finite bound, none can be infinite,
                // because mixed cases are not supported by the current code.
                throw new MathUnsupportedOperationException();
            }
            // Check if the difference between upper and lower bound is too large
            double boundDifference = uB[i] - lB[i];
            if (boundDifference > Double.MAX_VALUE) {
                throw new MathUnsupportedOperationException("Difference between upper and lower bounds is too large at index " + i);
            }
        }
    } else {
        // If no finite bounds, set boundaries to null
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
            if (boundaries != null && inputSigma[i] > boundaries[1][i] - boundaries[0][i]) {
                throw new OutOfRangeException(inputSigma[i], 0, boundaries[1][i] - boundaries[0][i]);
            }
        }
    }
}