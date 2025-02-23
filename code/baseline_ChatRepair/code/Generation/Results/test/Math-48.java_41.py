protected final double doSolve() {
    // Initialize variables
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    double f1 = computeObjectiveValue(x1);

    // Check direct solutions on bounds
    if (f0 == 0.0) {
        return x0;
    }
    if (f1 == 0.0) {
        return x1;
    }

    // Verify the initial interval is valid
    verifyBracketing(x0, x1);

    // Get accuracies
    final double ftol = getFunctionValueAccuracy();
    final double atol = getAbsoluteAccuracy();
    final double rtol = getRelativeAccuracy();

    // Iteration limit
    final int maxIterations = getMaxEvaluations();
    int iterationCount = 0;

    while (iterationCount < maxIterations) {
        iterationCount++;

        // Calculate the next point
        final double x = x1 - f1 * (x1 - x0) / (f1 - f0);
        final double fx = computeObjectiveValue(x);

        // Check for exact root
        if (fx == 0.0) {
            return x;
        }

        // Update the bounds
        if (f1 * fx < 0) {  // Root is between x and x1
            x0 = x1;
            f0 = f1;
        } else {
            switch (method) {
            case ILLINOIS:
                f0 *= 0.5;
                break;
            case PEGASUS:
                f0 *= f1 / (f1 + fx);
                break;
            case REGULA_FALSI:
                break;            
            default:
                throw new MathInternalError();
            }
        }
        x1 = x;
        f1 = fx;

        // Check for convergence
        if (Math.abs(f1) <= ftol || Math.abs(x1 - x0) <= Math.max(rtol * Math.abs(x1), atol)) {
            return x1;
        }
    }

    throw new TooManyEvaluationsException(maxIterations);
}
