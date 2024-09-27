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

    // Keep finding better approximations.
    while (true) {
        // Calculate the next approximation.
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);

        // If the new approximation is the exact root, return it. Since
        // this is not an under-approximation or an over-approximation,
        // we can return it regardless of the allowed solutions.
        if (fx == 0.0) {
            return x;
        }

        // Update the bounds with the new approximation.
        if (f1 * fx < 0) {
            // The value of x1 has switched to the other bound, thus inverting
            // the interval.
            x0 = x1;
            f0 = f1;
            inverted = !inverted;
        } else {
            // Adjust f0 according to the method
            f0 = adjustF0(f0, f1, fx);
        }
        // Update from [x0, x1] to [x0, x].
        x1 = x;
        f1 = fx;

        // Check if the function value of the last approximation is too small,
        // given the function value accuracy.
        if (Math.abs(f1) <= ftol) {
            return decideAccordingToAllowedSide(x1, f1, inverted);
        }

        // Check if the current interval is within the given accuracies.
        if (Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return decideAccordingToInterval(x0, x1, f1, inverted);
        }
    }
}

private double adjustF0(double f0, double f1, double fx) {
    switch (method) {
        case ILLINOIS:
            return f0 * 0.5;
        case PEGASUS:
            return f0 * f1 / (f1 + fx);
        case REGULA_FALSI:
            // Custom logic for REGULA_FALSI if necessary, currently no change.
            return f0;
        default:
            throw new MathInternalError();
    }
}

private double decideAccordingToAllowedSide(double x1, double f1, boolean inverted) {
    switch (allowed) {
        case ANY_SIDE:
            return x1;
        case LEFT_SIDE:
            return inverted ? x1 : throw new MathInternalError(); // left side logic
        case RIGHT_SIDE:
            return !inverted ? x1 : throw new MathInternalError(); // right side logic
        case BELOW_SIDE:
            return f1 <= 0 ? x1 : throw new MathInternalError(); // below side logic
        case ABOVE_SIDE:
            return f1 >= 0 ? x1 : throw new MathInternalError(); // above side logic
        default:
            throw new MathInternalError();
    }
}

private double decideAccordingToInterval(double x0, double x1, double f1, boolean inverted) {
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