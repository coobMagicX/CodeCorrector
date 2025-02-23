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
    double lastIntervalSize = Double.POSITIVE_INFINITY;

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
            switch (method) {
            case ILLINOIS:
                f0 *= 0.5;
                break;
            case PEGASUS:
                f0 *= f1 / (f1 + fx);
                break;
            case REGULA_FALSI:
                // Avoiding multiple updates leading to tiny interval
                if (Math.abs(x - x1) < 1e-10 * Math.abs(x1)) {
                    f0 = f1;
                    break;
                }
                break;
            default:
                throw new MathInternalError();
            }
        }

        x1 = x;
        f1 = fx;

        if (Math.abs(f1) <= ftol) {
            return handleExtraConditions(x1, f1, inverted);
        }

        double currentIntervalSize = Math.abs(x1 - x0);
        if (currentIntervalSize >= lastIntervalSize) {
            // Interval size is not decreasing, break out of loop
            throw new ConvergenceException(LocalizedFormats.CONVERGENCE_FAILED);
        }
        lastIntervalSize = currentIntervalSize;

        if (currentIntervalSize < Math.max(rtol * Math.abs(x1), atol)) {
            return handleExtraConditions(x1, f1, inverted);
        }
    }
}

private double handleExtraConditions(double x1, double f1, boolean inverted) {
    switch (allowed) {
    case ANY_SIDE:
        return x1;
    case LEFT_SIDE:
        return inverted ? x1 : getMin();
    case RIGHT_SIDE:
        return inverted ? getMax() : x1;
    case BELOW_SIDE:
        return (f1 <= 0) ? x1 : getMin();
    case ABOVE_SIDE:
        return (f1 >= 0) ? x1 : getMax();
    default:
        throw new MathInternalError();
    }
}
