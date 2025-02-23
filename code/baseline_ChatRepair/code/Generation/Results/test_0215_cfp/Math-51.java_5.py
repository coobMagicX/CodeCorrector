protected final double doSolve() {
    // Initialization with bounds and respective function values
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

    while (true) {
        final double x = x1 - (f1 * (x1 - x0)) / (f1 - f0);
        final double fx = computeObjectiveValue(x);

        // Direct root check
        if (fx == 0.0) {
            return x;
        }

        // Update interval
        if ((f1 * fx) < 0) {
            x0 = x1;  // x1 becomes the new x0
            f0 = f1;
        } else {
            // Reduction in the effect of the previous point `x0`
            if (method == Method.ILLINOIS || method == Method.PEGASUS) {
                f0 *= (method == Method.PEGASUS) ? f1 / (f1 + fx) : 0.5;
            }
        }
        
        x1 = x;
        f1 = fx;

        // Checking function value to be within tolerance
        if (Math.abs(f1) <= ftol) {
            return determineReturn(x1, inverted);
        }

        // Interval convergence check
        if (Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return determineReturn(x1, inverted);
        }
    }
}

private double determineReturn(double x1, boolean inverted) {
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
