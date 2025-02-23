protected final double doSolve() throws ConvergenceException, TooManyEvaluationsException {
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
    int maxIterations = getMaxIterations();
    int iterations = 0;

    while (iterations < maxIterations) {
        
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
            // Handle method-specific cases as in the original code.
        }

        x1 = x;
        f1 = fx;

        if (FastMath.abs(f1) <= ftol) {
            // Handle allowed solution types as in the original code.
        }

        if (FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
            // Handle allowed solution types as in the original code.
        }

        iterations++;
    }

    throw new ConvergenceException(LocalizedFormats.CONVERGENCE_FAILED, iterations, x0, x1);
}
