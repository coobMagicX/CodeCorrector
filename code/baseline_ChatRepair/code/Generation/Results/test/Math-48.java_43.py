protected final double doSolve() throws ConvergenceException {
    final int maxIterations = getMaxEvaluations(); // maximum allowable iterations
    int iterations = 0; // current iteration

    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    double f1 = computeObjectiveValue(x1);

    verifyBracketing(x0, x1);
    
    final double ftol = getFunctionValueAccuracy();
    final double atol = getAbsoluteAccuracy();
    final double rtol = getRelativeAccuracy();

    boolean inverted = false;

    while (true) {
        iterations++; // Increment the count of iterations
        if (iterations > maxIterations) {
            throw new ConvergenceException(LocalizedFormats.FAILED_CONVERGENCE, maxIterations);
        }

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
                case REGULA_FALSI:
                    // Detect stuck algorithm
                    break;
                default:
                    throw new MathInternalError();
            }
        }

        x1 = x;
        f1 = fx;

        if (Math.abs(f1) <= ftol) {
            if (allowedConditionsMet(inverted, f1)) {
                return x1;
            }
        }

        if (Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            if (allowedConditionsMet(inverted, f1)) {
                return x1;
            }
        }
    }
}

private boolean allowedConditionsMet(boolean inverted, double f1) {
    // Add additional conditions check here based on requirement (`allowed`)
    return true;
}
