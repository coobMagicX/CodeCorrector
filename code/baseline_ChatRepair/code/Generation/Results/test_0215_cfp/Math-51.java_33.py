protected final double doSolve() {
    // Get initial solution along with verification.
    double x0 = getMin();
    double x1 = getMax();
    checkEvaluationCount();
    double f0 = computeObjectiveValue(x0);
    checkEvaluationCount();
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
        checkEvaluationCount(); // Check number of evaluations prior to each new estimation loop
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
            switch (method) {
            case ILLINOIS:
                f0 *= 0.5;
                break;
            case PEGASUS:
                f0 *= f1 / (f1 + fx);
                break;
            }
        }

        x1 = x;
        f1 = fx;

        if (Math.abs(f1) <= ftol) {
            return resolveFinalResult(x1, inverted, f1);
        }

        if (Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return resolveFinalResult(x1, inverted, f1);
        }
    }
}

private void checkEvaluationCount() {
    // Increment and check the count against a maximum allowed evaluations.
    if (++evaluationCount > maxEvaluations) {
        throw new TooManyEvaluationsException(maxEvaluations);
    }
}

private double resolveFinalResult(double x1, boolean inverted, double f1) {
    // This method could combine and manage resolving the output based on f1, inverted, and other conditions.
    // Implementation would depend on specifics currently not provided.
    return x1;  // This needs more specific implementation details.
}
