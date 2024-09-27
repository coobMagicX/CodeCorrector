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
                if (inputSigma[i] > boundaries[1][i] - boundaries[0][i]) {
                    throw new OutOfRangeException(inputSigma[i], 0, boundaries[1][i] - boundaries[0][i]);
                }
            }
        }
    }
    
    // Repair the inputSigma array if necessary
    if (inputSigma != null) {
        inputSigma = repair(inputSigma);
    }
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

@Test
public void testBoundaryRangeTooLarge() {
    double[] start = {0.5, 0.5};
    double[] lowerBound = {0.0, 0.0};
    double[] upperBound = {2.0, 2.0};
    double[] sigma = {1.0, 1.0};

    CMAESOptimizer optimizer = new CMAESOptimizer();
    optimizer.setStartPoint(start);
    optimizer.setLowerBound(lowerBound);
    optimizer.setUpperBound(upperBound);
    optimizer.setInputSigma(sigma);

    try {
        optimizer.optimize(new MaxEval(100), new SimpleBounds(lowerBound, upperBound));
        fail("Expected OutOfRangeException");
    } catch (OutOfRangeException e) {
        // expected
    }
}