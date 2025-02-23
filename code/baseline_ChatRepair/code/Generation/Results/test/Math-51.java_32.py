protected final double doSolve() {
    // Get initial solution
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    double f1 = computeObjectiveValue(x1);

    // If one bound is the exact root, return it.
    if (f0 == 0.0) {
        return x0;
    }
    if (f1 == 0.0) {
        return x1;
    }

    verifyBracketing(x0, x1);

    // Get accuracies.
    final double ftol = getFunctionValueAccuracy();
    final double atol = getAbsoluteAccuracy();
    final double rtol = getRelativeAccuracy();

    boolean inverted = false;
    final int maxEval = getMaxEvaluations();  // Assuming getMaxEvaluations() exist
    int evalCount = 2; // we have already used 2 evaluations above

    while (evalCount < maxEval) {
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);
        evalCount++;

        if (fx == 0.0) {
            return x;
        }

        // Update bounds
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
            default:
                // Should never happen.
            }
        }

        x1 = x;
        f1 = fx;

        // Check if the function value is close enough to zero
        if (Math.abs(f1) <= ftol) {
            return findAllowedRoot(x1, f1, inverted);
        }

        // Check if the search interval is small enough
        if (Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return findAllowedRoot(x1, f1, inverted);
        }
    }

    // Throw an exception indicating that the number of maximum evaluations has been exceeded
    throw new TooManyEvaluationsException(maxEval);
}

private double findAllowedRoot(double x1, double f1, boolean inverted) {
    // A method to determine which root to return based on allowed solutions.
    // Implementation is based on your custom determination logic from the initial solve method.
    return x1; // Modify according to your determination logic.
}
