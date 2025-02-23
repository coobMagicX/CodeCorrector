protected final double doSolve() {
    // Get maximum evaluations count
    final int maxEvaluations = getMaxEvaluations();
    int evaluations = 0;

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

    while (true) {
        if (evaluations >= maxEvaluations) {
            throw new TooManyEvaluationsException(maxEvaluations);
        }

        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);
        evaluations++;

        if (fx == 0.0) {
            return x;
        }

        if (f1 * fx < 0) {
            x0 = x1;
            f0 = f1;
            inverted = !inverted;
        } else {
            // Update f0 according to the root-finding algorithm used
            f0 = updateF0(f0, f1, fx);
        }

        x1 = x;
        f1 = fx;

        if (Math.abs(f1) <= ftol) {
            return handleToleranceOptions(x1, inverted, f1);
        }

        if (Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return handleIntervalConvergence(x0, x1, inverted, f1);
        }
    }
}

private double updateF0(double f0, double f1, double fx) {
    switch (method) {
    case ILLINOIS:
        return f0 * 0.5;
    case PEGASUS:
        return f0 * f1 / (f1 + fx);
    default:
        throw new MathInternalError();
    }
}

private double handleToleranceOptions(double x1, boolean inverted, double f1) {
    // Implementation according to requirements. Example:
    switch (allowed) {
    case ANY_SIDE:
        return x1;
    case LEFT_SIDE:
        if (inverted) {
            return x1;
        }
        break;
    case RIGHT_SIDE:
        if (!inverted) {
            return x1;
        }
        break;
    // Add more cases as required.
    default:
        throw new MathInternalError();
    }
    return x1; // or appropriate default value.
}

private double handleIntervalConvergence(double x0, double x1, boolean inverted, double f1) {
    // Handle interval convergence based on allowed side and inversion
    switch (allowed) {
    case ANY_SIDE:
        return x1;
    case LEFT_SIDE:
        return inverted ? x1 : x0;
    case RIGHT_SIDE:
        return inverted ? x0 : x1;
    // Add more as required.
    default:
        throw new MathInternalError();
    }
}
