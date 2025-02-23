protected final double doSolve() {
    // Initialize count of function evaluations
    int evaluationCount = 0;
    final int maxEvaluations = getMaxEvaluations();  // Assume this method returns the allowed maximum evaluations

    // Obtain the initial solution
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    evaluationCount++;
    double f1 = computeObjectiveValue(x1);
    evaluationCount++;

    // Check for exact root at the bounds
    if (f0 == 0.0) {
        return x0;
    }
    if (f1 == 0.0) {
        return x1;
    }

    // Verify the initial bracketing
    verifyBracketing(x0, x1);

    // Set accuracies
    final double ftol = getFunctionValueAccuracy();
    final double atol = getAbsoluteAccuracy();
    final double rtol = getRelativeAccuracy();

    boolean inverted = false;

    // Loop until convergence or maximum evaluations are reached
    while (evaluationCount < maxEvaluations) {
        final double x = x1 - (f1 * (x1 - x0) / (f1 - f0));
        final double fx = computeObjectiveValue(x);
        evaluationCount++;

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
                    if (x == x1) {
                        x0 = 0.5 * (x0 + x1 - Math.max(rtol * Math.abs(x1), atol));
                        f0 = computeObjectiveValue(x0);
                        evaluationCount++;
                    }
                    break;
                default:
                    throw new MathInternalError();
            }
        }

        x1 = x;
        f1 = fx;

        if (Math.abs(f1) <= ftol || Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return x1; // Further refinement could consider allowed solutions as in the original code
        }

        if (evaluationCount >= maxEvaluations) {
            throw new TooManyEvaluationsException(maxEvaluations);
        }
    }

    throw new TooManyEvaluationsException(maxEvaluations);
}
