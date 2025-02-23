protected final double doSolve() {
    // Define the maximum number of iterations
    final int MAX_EVALUATIONS = 1000;
    int iterations = 0;

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
        if (iterations++ >= MAX_EVALUATIONS) {
            throw new TooManyEvaluationsException(MAX_EVALUATIONS);
        }

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
            switch (method) {
            case ILLINOIS:
                f0 *= 0.5;
                break;
            case PEGASUS:
                f0 *= f1 / (f1 + fx);
                break;
            default:
                // Should never happen.
            }
        }

        x1 = x;
        f1 = fx;

        if (FastMath.abs(f1) <= ftol) {
            return handleAllowedSides(x1, inverted, f1);
        }

        if (FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
            return handleAllowedSides(x1, inverted, f1);
        }
    }
}

private double handleAllowedSides(double x1, boolean inverted, double f1) {
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
