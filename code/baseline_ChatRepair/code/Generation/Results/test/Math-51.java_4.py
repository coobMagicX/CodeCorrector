protected final double doSolve() {
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

    double lastIntervalSize = Double.POSITIVE_INFINITY;
    int evaluationCount = 0, maxEvaluations = getMaxEvaluations();

    while (true) {
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);
        evaluationCount++;

        if (fx == 0.0 || evaluationCount > maxEvaluations) {
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
            default:
                //Should never happen.
            }
        }

        x1 = x;
        f1 = fx;

        if (FastMath.abs(f1) <= ftol) {
            return x1;
        }

        double currentIntervalSize = FastMath.abs(x1 - x0);
        if (currentIntervalSize < FastMath.max(rtol * FastMath.abs(x1), atol)) {
            return x1;
        }
        // Check if insufficient progress is being made
        if (currentIntervalSize >= lastIntervalSize * 0.995) {
            throw new ConvergenceException(LocalizedFormats.CONVERGENCE_FAILED);
        }
        lastIntervalSize = currentIntervalSize;
    }
}
