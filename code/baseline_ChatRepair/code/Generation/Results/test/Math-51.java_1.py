protected final double doSolve() {
    // Get initial solution
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    double f1 = computeObjectiveValue(x1);
    int maxEval = getMaxEvaluations();

    // Checking the exact root at the bounds.
    if (f0 == 0.0) return x0;
    if (f1 == 0.0) return x1;

    // Verify bracketing of initial solution.
    verifyBracketing(x0, x1);

    // Other initializations
    final double ftol = getFunctionValueAccuracy();
    final double atol = getAbsoluteAccuracy();
    final double rtol = getRelativeAccuracy();
    boolean inverted = false;

    // Count the evaluations
    int evalCount = 2; // We already know f0 and f1

    // Keep finding better approximations.
    while (evalCount <= maxEval) {
        // Calculate the next approximation.
        double x = x1 - (f1 * (x1 - x0) / (f1 - f0));
        double fx = computeObjectiveValue(x);
        evalCount++; // Incrementing the evaluations count

        // If the new approximation is the exact root or within tolerance, return it.
        if (fx == 0.0 || Math.abs(fx) <= ftol || Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            switch (allowed) {
            case ANY_SIDE:
                return x;
            case LEFT_SIDE:
                return inverted ? x1 : x0;
            case RIGHT_SIDE:
                return inverted ? x0 : x1;
            case BELOW_SIDE:
                return (f1 <= 0) ? x1 : x0;
            case ABOVE_SIDE:
                return (f1 >= 0) ? x1 : x0;
            default:
                throw new MathInternalError();
            }
        }

        // Update the bounds with the new approximation.
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
                // Update formula cannot make any progress: Update the
                // search interval.
            }
        }
        x1 = x;
        f1 = fx;
    }

    throw new TooManyEvaluationsException(maxEval);
}
