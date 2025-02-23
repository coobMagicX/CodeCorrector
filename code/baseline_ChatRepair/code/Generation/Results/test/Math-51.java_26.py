protected final double doSolve() {
    // Maximum number of iterations allowed - this could be another method parameter
    final int maxEvaluations = getMaxEvaluations();
    int evaluations = 0;

    // Existing initializations
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    double f1 = computeObjectiveValue(x1);
    evaluations += 2; // Two evaluations made above

    if (f0 == 0.0) {
        return x0;
    }
    if (f1 == 0.0) {
        return x1;
    }

    verifyBracketing(x0, x1);

    // Existing accuracies initialization
    final double ftol = getFunctionValueAccuracy();
    final double atol = getAbsoluteAccuracy();
    final double rtol = getRelativeAccuracy();

    boolean inverted = false;

    // Modifying loop to check the break condition based on evaluations
    while (evaluations < maxEvaluations) {
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0)); // compute x
        final double fx = computeObjectiveValue(x);
        evaluations++; // Increase count of evaluations

        if (fx == 0.0) {
            return x;
        }

        // Update bounds with the new approximation
        if (f1 * fx < 0) {
            x0 = x1;
            f0 = f1;
            inverted = !inverted;
        } else {
            // Use helper method to modify `f0`
            f0 = updateFunctionModifier(f0, f1, fx, method);
        }

        // Update interval
        x1 = x;
        f1 = fx;

        // Check function value accuracy
        if (Math.abs(f1) <= ftol) {
            return handleSide(f1, x0, x1, inverted, allowed);
        }

        // Check if interval is within the tolerance
        if (Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return handlePullOff(x0, x1, f0, f1, inverted, allowed, evaluations, maxEvaluations);
        }
    }

    throw new TooManyEvaluationsException(maxEvaluations);
}
