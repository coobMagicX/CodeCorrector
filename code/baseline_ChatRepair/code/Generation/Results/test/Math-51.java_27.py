protected final double doSolve() {
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    double f1 = computeObjectiveValue(x1);

    if (f0 == 0.0) {
        return x0;
    }
    if (f1 == 0.0) {
        return x1;
    }

    verifyBracketing(x0, x1);

    final double ftol = getFunctionValueAccuracy();
    final double atol = getAbsoluteAccuracy();
    final double rtol = getRelativeAccuracy();

    boolean inverted = false;

    while (true) {
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);

        if (fx == 0.0) {
            return x;
        }

        if (f1 * fx < 0) {
            x0 = x1;
            f0 = f1;
            inverted = !inverted;
        } else {
            if (Math.abs(f1 + fx) > Math.abs(f1)) {
                // Ensure we only make the adjustments if meaningful progress
                // can be made, preventing "zig-zag" issue.
                switch (method) {
                case ILLINOIS:
                    f0 *= 0.5;
                    break;
                case PEGASUS:
                    f0 *= f1 / (f1 + fx);
                    break;
                default:
                    break;
                }
            }
        }

        x1 = x;
        f1 = fx;

        if (Math.abs(f1) <= ftol) {
            return x1;
        }

        if (Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return x1;
        }

        // Add a safeguard to prevent excessive looping.
        checkConvergence(x0, x1, x, fx); // This method should throw an
                                        // exception if convergence issues are
                                        // detected based on internal criteria.
    }
}

private void checkConvergence(double oldX0, double oldX1, double newX, double newFx) {
    if (Math.abs(newX - oldX1) < 1e-15) {
        throw new NoConvergenceException(); // Define or use an appropriate exception type.
    }
    if (iterations++ > maxEvaluations) { // Verify if the iterations exceed allowable evaluations.
        throw new TooManyEvaluationsException(maxEvaluations);
    }
}
