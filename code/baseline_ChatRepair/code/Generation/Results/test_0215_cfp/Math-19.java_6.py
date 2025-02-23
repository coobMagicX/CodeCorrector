private void checkParameters() {
    final double[] init = getStartPoint();
    final double[] lB = getLowerBound();
    final double[] uB = getUpperBound();

    // Handling boundaries
    boundaries = new double[2][];
    for (int i = 0; i < lB.length; i++) {
        if (Double.isInfinite(lB[i]) && Double.isInfinite(uB[i])) {
            // If both are infinite, there might be specific handling or default values.
            continue;
        } else if ((Double.isInfinite(lB[i]) && !Double.isInfinite(uB[i])) ||
                   (!Double.isInfinite(lB[i]) && Double.isInfinite(uB[i]))) {
            // One side infinite case; continue as it's allowed by the system.
            continue;
        } else if (!Double.isInfinite(lB[i]) && !Double.isInfinite(uB[i])) {
            if (lB[i] >= uB[i]) {
                throw new NumberIsTooLargeException(lB[i], uB[i], false);
            }
        }
    }
    boundaries[0] = lB;
    boundaries[1] = uB;

    // Checking according to sigma
    if (inputSigma != null) {
        if (inputSigma.length != init.length) {
            throw new DimensionMismatchException(inputSigma.length, init.length);
        }
        for (int i = 0; i < init.length; i++) {
            if (inputSigma[i] < 0) {
                throw new NotPositiveException(inputSigma[i]);
            }
            if (boundaries != null && !Double.isInfinite(boundaries[0][i]) && !Double.isInfinite(boundaries[1][i])) {
                double range = boundaries[1][i] - boundaries[0][i];
                if (inputSigma[i] > range) {
                    throw new OutOfRangeException(inputSigma[i], 0, range);
                }
            }
        }
    } else {
        // Handling if there is no sigma provided
        // Set default or throw if essential
    }
}
