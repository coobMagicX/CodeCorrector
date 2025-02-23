protected final double doSolve() throws TooManyEvaluationsException {
    int maxEvaluations = 3624; // Maximum allowed evaluations
    int evalCount = 0; // Evaluation count
    
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0); evalCount++;
    double f1 = computeObjectiveValue(x1); evalCount++;

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
        if (evalCount >= maxEvaluations) {
            throw new TooManyEvaluationsException(maxEvaluations);
        }

        double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        double fx = computeObjectiveValue(x); evalCount++;

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
            return handleAccuracy(x1, inverted);
        }

        if (FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
            return handleAccuracy(x1, inverted);
        }
    }
}

private double handleAccuracy(double x, boolean inverted) {
    switch (allowed) {
        case ANY_SIDE:
            return x;
        case LEFT_SIDE:
            return inverted ? x : x0;
        case RIGHT_SIDE:
            return inverted ? x0 : x;
        case BELOW_SIDE:
            return (f1 <= 0) ? x : x0;
        case ABOVE_SIDE:
            return (f1 >= 0) ? x : x0;
        default:
            throw new MathInternalError();
    }
}
