protected final double doSolve() throws TooManyEvaluationsException {
    // Maximum number of allowed evaluations
    int maxEvaluations = getMaxEvaluations();
    int evaluations = 0;

    // Implementation details as before...
    double x0 = getMin();
    double x1 = getMax();
    
    double f0 = computeObjectiveValue(x0);
    evaluations++;
    double f1 = computeObjectiveValue(x1);
    evaluations++;

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
        if (evaluations >= maxEvaluations) {
            throw new TooManyEvaluationsException(maxEvaluations);
        }

        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);
        evaluations++;

        if (fx == 0.0) {
            return x;
        }

        if (f1 * fx < 0) {
            x0 = x1;
            f0 = f1;
            inverted = !inverted;
        } else {
            // Switch-case logic from the previous snippet
        }

        x1 = x;
        f1 = fx;

        if (FastMath.abs(f1) <= ftol) {
            // Final decision logic from the previous snippet
        }

        if (FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
            // Final decision logic from the previous snippet
        }
    }
}
