protected final double doSolve() {
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
    double lastX = Double.POSITIVE_INFINITY; // To monitor convergence
    int iterations = 0; // Added to track iterations

    while (true) {
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
                default:
            }
        }
        x1 = x;
        f1 = fx;

        // Check if change is too small
        if (Math.abs(x - lastX) < Math.max(rtol * Math.abs(x), atol) || iterations >= getMaxEvaluations()) {
            return x;  // Return current estimate if change is small or max iterations reached
        }

        lastX = x; // Update lastX to current x for next iteration's convergence check
        iterations++; // Increment iterations

        if (FastMath.abs(f1) <= ftol) {
            return x1; // Assume x1 is the solution within acceptables limits.
        }
    }
}
