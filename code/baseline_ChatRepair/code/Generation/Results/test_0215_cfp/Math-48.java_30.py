protected final double doSolve() {
    // Initialization and bounds setup remains the same.

    // Initialize necessary variables as before.
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

    double ftol = getFunctionValueAccuracy();
    double atol = getAbsoluteAccuracy();
    double rtol = getRelativeAccuracy();

    boolean inverted = false;

    // Improved stopping criteria.
    while (FastMath.abs(x1 - x0) > FastMath.max(rtol * FastMath.abs(x1), atol)) {
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
            f0 = updateFactor(method, f0, f1, fx);
        }

        x1 = x;
        f1 = fx;

        if (FastMath.abs(f1) <= ftol) {
            return handleAllowedSolutions(inverted, x0, x1, f1);
        }

        // To prevent the excessive evaluations and ensure convergence.
        checkConvergence(x0, x1, f0, f1);
    }

    return handleAllowedSolutions(inverted, x0, x1, f1);
}

private double updateFactor(Method method, double f0, double f1, double fx) {
    switch (method) {
        case ILLINOIS:
            return f0 * 0.5;
        case PEGASUS:
            return f0 * f1 / (f1 + fx);
        case REGULA_FALSI:
            // Implement specific method handling if necessary.
            return f0;
        default:
            throw new MathInternalError();
    }
}

private void checkConvergence(double x0, double x1, double f0, double f1) {
    // Implement based on the allowed number of evaluations and other criteria.
}

private double handleAllowedSolutions(boolean inverted, double x0, double x1, double f1) {
    // Implements the checks based on 'allowed' enum similar to original.
    // Return calculation result.
}

