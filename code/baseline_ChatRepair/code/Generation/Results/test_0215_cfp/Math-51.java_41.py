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
            // Reduce f0 if no crossing.
            f0 *= (method == PEGASUS) ? f1 / (f1 + fx) : 0.5;
        }

        x1 = x;
        f1 = fx;

        // Check if function is sufficiently close to zero
        if (Math.abs(f1) <= ftol) {
            return selectResultBasedOnAllowedSide(inverted, x1, x0, f1);
        }

        // Check for convergence of the interval within tolerances.
        if (Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return selectResultBasedOnAllowedSide(inverted, x1, x0, f1);
        }

        // Prevent too many evaluations or infinite loop
        if (getEvaluations() > getMaxEvaluations()) {
            throw new TooManyEvaluationsException(getMaxEvaluations());
        }
    }
}

private double selectResultBasedOnAllowedSide(boolean inverted, double x1, double x0, double f1) {
    switch (allowed) {
    case ANY_SIDE:
        return x1;
    case LEFT_SIDE:
        return inverted ? x1 : x0;
    case RIGHT_SIDE:
        return inverted ? x0 : x1;
    case BELOW_SIDE:
        return (f1 <= 0) ? x1 : x0;
    case ABOVE_SIDE:
        return (f1 >= 0) ? x1 : x0;
    default:
        throw new MathInternalError();
    }
}
