protected final double doSolve() {
    // Get initial solution
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    double f1 = computeObjectiveValue(x1);

    // If one of the bounds is the exact root, return it.
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

    // Initialize the previous approximation to be x0 initially.
    double xPrev = x0;
    // Keep finding better approximations.
    while (true) {
        // Calculate the next approximation using the Regula Falsi formula.
        final double x = x1 - ((f1 * (x1 - xPrev)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);

        // If the new approximation is the exact root, return it.
        if (fx == 0.0) {
            return x;
        }

        // Update the bounds with the new approximation and invert the interval if necessary.
        if (f1 * fx < 0) {
            xPrev = x1; // Update previous to last used value
            f0 = f1;    // Use current function value for last used value
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

        xPrev = x; // Update previous to current
        x1 = x;    // Set new approximation as upper bound
        f1 = fx;   // Use new function value for upper bound

        // Check if the last approximation is close enough to a root within accuracy limits.
        if (FastMath.abs(f1) <= ftol) {
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
            case BELOW_SIDE:
                if (f1 <= 0) {
                    return x1;
                }
                break;
            case ABOVE_SIDE:
                if (f1 >= 0) {
                    return x1;
                }
                break;
            default:
                throw new MathInternalError();
            }
        }

        // If the current interval is within the given accuracies, we
        // are satisfied with the current approximation.
        if (FastMath.abs(x1 - xPrev) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
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
    }
}