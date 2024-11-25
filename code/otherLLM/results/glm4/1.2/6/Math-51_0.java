protected final double doSolve() {
    // Get initial solution
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    double f1 = computeObjectiveValue(x1);

    // If one of the bounds is the exact root, return it.
    if (f0 == 0.0) {
        return x0;
    }
    if (f1 == 0.0) {
        return x1;
    }

    // Verify bracketing of initial solution.
    verifyBracketing(x0, x1);

    // Get accuracies.
    final double ftol = getFunctionValueAccuracy();
    final double atol = getAbsoluteAccuracy();
    final double rtol = getRelativeAccuracy();

    // Keep track of inverted intervals, meaning that the left bound is
    // larger than the right bound.
    boolean inverted = false;

    // Keep finding better approximations.
    while (true) {
        // Calculate the next approximation.
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);

        // If the new approximation is the exact root, return it.
        if (fx == 0.0) {
            return x;
        }

        // Update the bounds with the new approximation.
        if (f1 * fx < 0) {
            // The value of x1 has switched to the other bound, thus inverting
            // the interval.
            x0 = x1;
            f0 = f1;
            inverted = !inverted;
        } else {
            switch (method) {
                case ILLINOIS:
                    updateBoundsIllinois(x, f1);
                    break;
                case PEGASUS:
                    updateBoundsPegasus(x, f1);
                    break;
                default:
                    // Should never happen.
                    throw new MathInternalError();
            }
        }

        x1 = x;
        f1 = fx;

        // If the function value of the last approximation is too small,
        // given the function value accuracy, then we can't get closer to
        // the root than we already are.
        if (FastMath.abs(f1) <= ftol) {
            return determineRoot(x1, inverted);
        }

        // If the current interval is within the given accuracies, we
        // are satisfied with the current approximation.
        if (FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
            return determineRoot(x1, inverted);
        }
    }
}

private void updateBoundsIllinois(double x, double f1) {
    f0 *= 0.5;
}

private void updateBoundsPegasus(double x, double f1) {
    f0 *= f1 / (f1 + fx);
}