protected final double doSolve() {
    // Retrieve maximum evaluations count
    int maxEval = getMaxEval();

    // Initialize evaluations count
    int evalCount = 0;

    // Get initial solution
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    evalCount++;
    double f1 = computeObjectiveValue(x1);
    evalCount++;

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

    while (evalCount < maxEval) {
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);
        evalCount++;

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
                // Other cases handling...
            }
        }
        x1 = x;
        f1 = fx;

        if (Math.abs(f1) <= ftol) {
            return resolveByAllowedSide(x1, f1, inverted);
        }

        if (Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return resolveByAllowedSide(x1, f1, inverted);
        }
    }

    throw new TooManyEvaluationsException(maxEval);
}

private double resolveByAllowedSide(double x, double fx, boolean inverted) {
    switch (allowed) {
    case ANY_SIDE:
        return x;
    case LEFT_SIDE:
        return inverted ? x : x0;
    case RIGHT_SIDE:
        return inverted ? x0 : x;
    case BELOW_SIDE:
        return fx <= 0 ? x : x0;
    case ABOVE_SIDE:
        return fx >= 0 ? x : x0;
    default:
        throw new MathInternalError();
    }
}
