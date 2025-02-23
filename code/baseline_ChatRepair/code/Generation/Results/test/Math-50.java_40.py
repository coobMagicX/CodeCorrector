protected final double doSolve() {
    int maxEvaluations = getMaxEvaluations(); // or some suitable method to get the maximum number of allowable evaluations.
    int evaluations = 0;

    // Get initial solution
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    evaluations++;
    double f1 = computeObjectiveValue(x1);
    evaluations++;

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

    while (evaluations < maxEvaluations) {
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);
        evaluations++;

        // Check for exact solution
        if (fx == 0.0) {
            return x;
        }

        // Update the bounds with the new approximation.
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
                case REGULA_FALSI:
                    if (x == x1) {
                        x0 = 0.5 * (x0 + x1 - FastMath.max(rtol * FastMath.abs(x1), atol));
                        f0 = computeObjectiveValue(x0);
                        evaluations++;
                    }
                    break;
                default:
                    throw new MathInternalError();
            }
        }

        x1 = x;
        f1 = fx;

        // Functional value accuracy check
        if (FastMath.abs(f1) <= ftol) {
            return conditionalReturn(x1, inverted);
        }

        // Relative and absolute accuracy check
        if (FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
            return conditionalReturn(x1, inverted);
        }

        // Abort if allowed evaluations are exceeded
        if (evaluations >= maxEvaluations) {
            throw new TooManyEvaluationsException(maxEvaluations);
        }
    }
    throw new TooManyEvaluationsException(maxEvaluations);
}

private double conditionalReturn(double x1, boolean inverted) {
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
