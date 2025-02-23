protected final double doSolve() {
    // Initialization
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
            // Check if negligible progress, then exit with current best guess
            if (Math.abs(x - x1) / Math.abs(x1) < atol) {
                return x1;
            }
        }

        x1 = x;
        f1 = fx;

        // Stop if function value is sufficiently small or if changes are below threshold
        if (Math.abs(f1) <= ftol || Math.abs(x1 - x0) <= Math.max(rtol * Math.abs(x1), atol)) {
            return x1;
        }

        // Add a limit on iterations or evaluations, possibly throwing TooManyEvaluationsException
        checkIterationCount();
    }
}

private void checkIterationCount() {
    if (++evaluationCount > maxEvaluations) {
        throw new TooManyEvaluationsException(maxEvaluations);
    }
}
