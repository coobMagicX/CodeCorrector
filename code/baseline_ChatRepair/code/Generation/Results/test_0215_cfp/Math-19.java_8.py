private void checkParameters() {
    final double[] init = getStartPoint();
    final double[] lB = getLowerBound();
    final double[] uB = getUpperBound();

    if (lB.length != uB.length) {
        throw new DimensionMismatchException(lB.length, uB.length);
    }
    if (init.length != lB.length) {
        throw new DimensionMismatchException(init.length, lB.length);
    }

    boundaries = new double[2][];
    boundaries[0] = lB;
    boundaries[1] = uB;

    // Check for valid bounds setup, throw NumberIsTooLargeException if lower bound is >= upper bound
    for (int i = 0; i < lB.length; i++) {
        if (!Double.isInfinite(lB[i]) && !Double.isInfinite(uB[i]) && lB[i] >= uB[i]) {
            throw new NumberIsTooLargeException(lB[i], uB[i], true);
        }
    }

    // Validate the inputSigma if specified, checking boundaries according to sigma
    if (inputSigma != null) {
        if (inputSigma.length != init.length) {
            throw new DimensionMismatchException(inputSigma.length, init.length);
        }
        for (int i = 0; i < init.length; i++) {
            if (inputSigma[i] < 0) {
                throw new NotPositiveException(inputSigma[i]);
            }
            if (!Double.isInfinite(lB[i]) && !Double.isInfinite(uB[i])) {
                double gap = uB[i] - lB[i];
                if (inputSigma[i] > gap) {
                    throw new OutOfRangeException(inputSigma[i], 0, gap);
                }
            }
        }
    }
}
