private void checkParameters() {
    final double[] init = getStartPoint();
    final double[] lB = getLowerBound();
    final double[] uB = getUpperBound();

    // Initialize boundary array
    boundaries = new double[2][];
    boundaries[0] = lB;
    boundaries[1] = uB;

    // Checks evidence of invalid bounds setup
    for (int i = 0; i < lB.length; i++) {
        if (!Double.isInfinite(lB[i]) && !Double.isInfinite(uB[i]) && lB[i] >= uB[i]) {
            throw new NumberIsTooLargeException(lB[i], uB[i], false);
        }
    }

    // Validate inputSigma if it exists
    if (inputSigma != null) {
        if (inputSigma.length != init.length) {
            throw new DimensionMismatchException(inputSigma.length, init.length);
        }
        for (int i = 0; i < init.length; i++) {
            double range = boundaries[1][i] - boundaries[0][i];
            if (inputSigma[i] < 0) {
                throw new NotPositiveException(inputSigma[i]);
            }
            // Ensure sigma does not exceed the range between bounds when both bounds are finite
            if (!Double.isInfinite(lB[i]) && !Double.isInfinite(uB[i]) && inputSigma[i] > range) {
                throw new OutOfRangeException(inputSigma[i], 0, range);
            }
        }
    }
}
