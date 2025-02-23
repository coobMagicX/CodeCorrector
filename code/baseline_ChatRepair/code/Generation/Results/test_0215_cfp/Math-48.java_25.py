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
    final double atol = getAbsoluteAccuracy();
    final double rtol = getRelativeAccuracy();

    // Prepare for main iteration loop
    double fm = Double.POSITIVE_INFINITY;
    double x = Double.NaN; // last compute x
    
    while (true) {
        // Stopping condition: if function evaluations are too many, throw TooManyEvaluationsException
        incrementEvaluationCount();

        // Calculate new midpoint for potential x
        x = 0.5 * (x0 + x1);
        fm = computeObjectiveValue(x);

        // Check if current x is the solution
        if (fm == 0.0 || FastMath.abs(fm) < getFunctionValueAccuracy() || FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
            return x;
        }

        // Update the bounds
        if (Math.signum(fm) != Math.signum(f0)) {
            x1 = x;
            f1 = fm;
        } else {
            x0 = x;
            f0 = fm;
        }
    }
}
