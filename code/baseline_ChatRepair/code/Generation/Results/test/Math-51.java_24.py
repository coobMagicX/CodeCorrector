protected final double doSolve() {
    // Get initial solution
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
        double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        double fx = computeObjectiveValue(x);

        if (fx == 0.0) {
            return x;
        }

        if (f1 * fx < 0) {
            x0 = x1;
            f0 = f1;
            inverted = !inverted;
        } else if (method == Method.ILLINOIS || method == Method.PEGASUS) {
            f0 *= (method == Method.ILLINOIS) ? 0.5 : f1 / (f1 + fx);
        }

        x1 = x;
        f1 = fx;

        if (Math.abs(f1) <= ftol) {
            return selectResultBasedOnAllowedSide(x1, inverted, f1);
        }

        if (Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return selectResultBasedOnAllowedSide(x1, inverted, f1);
        }

        // Check after each loop. Prevent exceeding max evaluations.
        incrementEvaluationCount();
    }
}

private void incrementEvaluationCount() {
    if (++evaluationCount > maxEvaluations) {
        throw new TooManyEvaluationsException(maxEvaluations);
    }
}

private double selectResultBasedOnAllowedSide(double x, boolean inverted, double f) {
    switch (allowed) {
        case ANY_SIDE:
            return x;
        case LEFT_SIDE:
            return inverted ? x : x0;
        case RIGHT_SIDE:
            return inverted ? x0 : x;
        case BELOW_SIDE:
            return (f <= 0) ? x : x0;
        case ABOVE_SIDE:
            return (f >= 0) ? x : x0;
        default:
            throw new MathInternalError();
    }
}
