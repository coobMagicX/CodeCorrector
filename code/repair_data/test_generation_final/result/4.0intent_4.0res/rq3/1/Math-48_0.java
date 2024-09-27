protected final double doSolve() {
    // Get initial solution
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    double f1 = computeObjectiveValue(x1);

    // If one of the bounds is the exact root, return it. Since these are
    // not under-approximations or over-approximations, we can return them
    // regardless of the allowed solutions.
    if (f0 == 0.0) {
        return x0;
    }
    if (f1 == 0.0) {
        return x1;
    }

    // Verify bracketing of initial solution.
    verifyBracketing(x0, x1);

    // Get accuracies.
    final double ftol = getFunctionValueAccuracy();
    final double atol = getAbsoluteAccuracy();
    final double rtol = getRelativeAccuracy();

    // Keep track of inverted intervals, meaning that the left bound is
    // larger than the right bound.
    boolean inverted = false;

    while (true) {
        // Calculate the next approximation.
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);

        // If the new approximation is the exact root, return it.
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
                break;
            default:
                throw new MathInternalError();
            }
        }

        x1 = x;
        f1 = fx;

        if (Math.abs(f1) <= ftol) {
            return checkConvergenceCriterion(x1, inverted);
        }

        if (Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return checkConvergenceCriterion(x1, inverted);
        }
    }
}

private double checkConvergenceCriterion(double x1, boolean inverted) {
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