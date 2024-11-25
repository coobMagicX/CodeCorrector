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

    // Keep finding better approximations.
    while (true) {
        // Calculate the next approximation.
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);

        // If the new approximation is the exact root, return it.
        if (fx == 0.0) {
            return x;
        }

        // Update the bounds with the new approximation.
        boolean updateBounds = false;
        if (f1 * fx < 0) {
            x0 = x1;
            f0 = f1;
            inverted = !inverted;
            updateBounds = true;
        } else {
            switch (method) {
            case ILLINOIS:
                // Ensure the Illinois method does not lose information
                if (f0 == 0.0) {
                    x0 = x1;
                    f0 = fx;
                } else {
                    x1 = x;
                    f1 = fx;
                }
                break;
            case PEGASUS:
                // Prevent division by zero in the pegasus method
                if (f1 + fx != 0.0) {
                    double beta = f1 / (f1 + fx);
                    x1 = x0 - ((beta * (x0 - x1)) / (2 - beta));
                    f1 = computeObjectiveValue(x1);
                }
                break;
            default:
                // Should never happen.
            }
        }

        // If we updated bounds, proceed to the next iteration
        if (!updateBounds) {
            switch (method) {
            case ILLINOIS:
                x0 = x1;
                f0 = f1;
                break;
            case PEGASUS:
                x1 = x;
                f1 = fx;
                break;
            }
        }

        // Update from [x0, x1] to [x0, x].
        if (updateBounds) {
            x1 = x;
            f1 = fx;
        } else {
            x0 = x;
            f0 = fx;
        }

        // Check for convergence.
        if ((FastMath.abs(f1) <= ftol) || (FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol))) {
            break;
        }
    }

    return inverted ? x0 : x1;
}