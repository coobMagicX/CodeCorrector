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
            f0 = (method == Method.PEGASUS) ? f0 * f1 / (f1 + fx) : f0 * 0.5;
        }
        // Update from [x0, x1] to [x0, x].
        x1 = x;
        f1 = fx;

        // Check if an underflow occurs in the denominator
        if (Math.abs(f1 - f0) < Double.MIN_NORMAL) {
            if (allowed == AllowedSolution.ANY_SIDE) {
                return x1;
            } else if (allowed == AllowedSolution.LEFT_SIDE) {
                return inverted ? x1 : x0;
            } else if (allowed == AllowedSolution.RIGHT_SIDE) {
                return inverted ? x0 : x1;
            } else if (allowed == AllowedSolution.BELOW_SIDE) {
                return f1 <= 0 ? x1 : x0;
            } else if (allowed == AllowedSolution.ABOVE_SIDE) {
                return f1 >= 0 ? x1 : x0;
            }
        }

        // If the function value of the last approximation is too small,
        // given the function value accuracy, then we can't get closer to
        // the root than we already are.
        if (Math.abs(f1) <= ftol) {
            if (allowed == AllowedSolution.ANY_SIDE || (allowed == AllowedSolution.LEFT_SIDE && inverted) ||
                (allowed == AllowedSolution.RIGHT_SIDE && !inverted) || (allowed == AllowedSolution.BELOW_SIDE && f1 <= 0) ||
                (allowed == AllowedSolution.ABOVE_SIDE && f1 >= 0)) {
                return x1;
            }
        }

        // If the current interval is within the given accuracies, we
        // are satisfied with the current approximation.
        if (Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            if (allowed == AllowedSolution.ANY_SIDE) {
                return x1;
            } else if (allowed == AllowedSolution.LEFT_SIDE) {
                return inverted ? x1 : x0;
            } else if (allowed == Allowed Solution.RIGHT_SIDE) {
                return inverted ? x0 : x1;
            } else if (allowed == AllowedSolution.BELOW_SIDE) {
                return f1 <= 0 ? x1 : x0;
            } else if (allowed == AllowedSolution.ABOVE_SIDE) {
                return f1 >= 0 ? x1 : x0;
            }
        }
    }
}