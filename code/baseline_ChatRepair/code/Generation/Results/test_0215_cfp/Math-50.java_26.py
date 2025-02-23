protected final double doSolve() throws TooManyEvaluationsException {
    // Get maximum number of evaluations and set evaluation counter
    final int maxEvaluations = getMaxEvaluations();
    int evaluations = 0;

    // Get initial solution
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    evaluations++; // increment evaluations counter
    double f1 = computeObjectiveValue(x1);
    evaluations++; // increment evaluations counter

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
        if (evaluations >= maxEvaluations) {
            throw new TooManyEvaluationsException(maxEvaluations);
        }

        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);
        evaluations++; // increment evaluations counter

        if (fx == 0.0) {
            return x;
        }

        if (f1 * fx < 0) {
            x0 = x1;
            f0 = f1;
            inverted = !inverted;
        } else {
            // Method-specific adjustment
        }

        x1 = x;
        f1 = fx;

        if (FastMath.abs(f1) <= ftol) {
            return handleFinalReturnConditions(x1, inverted);
        }

        if (FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
            return handleFinalReturnConditions(x1, inverted);
        }
    }
}

private double handleFinalReturnConditions(double x1, boolean inverted) {
    switch (allowed) {
    case ANY_SIDE:
        return x1;
    case LEFT_SIDE:
        return inverted ? x1 : x0;
    case RIGHT_SIDE:
        return inverted ? x0 : x1;
    case BELOW_SIDE:
        return f1 <= 0 ? x1 : x0;
    case ABOVE_SIDE:
        return f1 >= 0 ? x1 : x0;
    default:
        throw new MathInternalError();
    }
}
