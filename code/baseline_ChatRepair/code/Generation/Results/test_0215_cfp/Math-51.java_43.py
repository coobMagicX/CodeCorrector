protected final double doSolve() throws TooManyEvaluationsException {
    // Get maximum number of evaluations
    final int maxEvaluations = getMaxEvaluations();
    int evaluations = 0;

    // Get initial solution
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    evaluations++;
    double f1 = computeObjectiveValue(x1);
    evaluations++;

    // If one of the bounds is the exact root, return it.
    if (f0 == 0.0) {
        return x0;
    }
    if (f1 == 0.0) {
        return x1;
    }

    verifyBracketing(x0, x1);

    // Get accuracies
    final double ftol = getFunctionValueAccuracy();
    final double atol = getAbsoluteAccuracy();
    final double rtol = getRelativeAccuracy();

    boolean inverted = false;

    while (true) {
        if (evaluations >= maxEvaluations) {
            throw new TooManyEvaluationsException(maxEvaluations);
        }

        final double x = x1 - (f1 * (x1 - x0) / (f1 - f0));
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
            // Existing method-dependent updates go here
        }

        x1 = x;
        f1 = fx;

        if (Math.abs(f1) <= ftol) {
            return handleSideCriteria(f1, x1, inverted);
        }

        if (Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return handleSideCriteria(f1, x1, inverted);
        }
    }
}

private double handleSideCriteria(double f1, double x1, boolean inverted) {
    // Implementation of your allowed solution side handling goes here.
    // This is an abstraction of the case handling in your original code.
    return x1;
}
