protected final double doSolve() {
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    double f1 = computeObjectiveValue(x1);

    if (f0 == 0.0) return x0;
    if (f1 == 0.0) return x1;

    verifyBracketing(x0, x1);

    final double ftol = getFunctionValueAccuracy();
    final double atol = getAbsoluteAccuracy();
    final double rtol = getRelativeAccuracy();

    while (true) {
        // Secant formula: (Improved calculation)
        final double x = x0 - f0 * (x1 - x0) / (f1 - f0);
        final double fx = computeObjectiveValue(x);

        if (fx == 0.0) {
            return x;
        }

        if (signum(f0) != signum(fx)) {
            x1 = x;
            f1 = fx;
        } else {
            x0 = x;
            f0 = fx;
        }

        // Check for sufficient reduction in function value
        if (Math.abs(fx) <= ftol) {
            return x;
        }
        
        // Check if converged within tolerances
        if (Math.abs(x1 - x0) <= Math.max(rtol * Math.abs(x1), atol)) {
            return 0.5 * (x0 + x1);
        }
    }
}

/** Helper to consistently determine sign of double values */
private static int signum(double value) {
    return (value < 0) ? -1 : (value > 0) ? 1 : 0;
}
