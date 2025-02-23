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

    // Verify bracketing
    if (f0 * f1 >= 0) {
        throw new NonBracketingException(x0, x1, f0, f1);
    }

    // Get accuracies.
    final double ftol = getFunctionValueAccuracy();
    final double atol = getAbsoluteAccuracy();
    final double rtol = getRelativeAccuracy();

    // Correctly tracking whether the interval is inverted
    boolean inverted = f0 > 0;

    // Main iteration loop
    while (true) {
        // Calculate midpoint
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);

        // Check for exact root
        if (fx == 0.0) {
            return x;
        }

        // Update bounds
        if (f1 * fx < 0) {
            x0 = x1;
            f0 = f1;
        } else {
            // Modify f0 according to the method used
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

        // Update x1 and f1
        x1 = x;
        f1 = fx;

        // Check if function value is close enough to zero
        if (Math.abs(f1) <= ftol) {
            return x1;
        }

        // Check if the result converged sufficiently for given accuracies
        if (Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return x1;
        }
    }
}
