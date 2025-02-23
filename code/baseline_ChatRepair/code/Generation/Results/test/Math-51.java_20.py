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

    while (true) {
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);

        if (fx == 0.0) {
            return x;
        }

        if (f1 * fx < 0) { // Sign change detected, meaning root is bracketed
            x0 = x1; // Assign the previous upper bound to the lower bound
            f0 = f1; // Assign the previous upper value to the lower value
            inverted = !inverted;
        } else {
            if (Math.abs(f1) > Math.abs(fx)) {  
                x0 = x; // Root is closer to x than x1
                f0 = fx;
            }
        }

        x1 = x; // New upper bound
        f1 = fx;

        // Check if solution satisfies error bounds
        if (Math.abs(f1) <= ftol || Math.abs(x1 - x0) <= Math.max(rtol * Math.abs(x1), atol)) {
            return x1;
        }

        // Ensure the loop doesn't run indefinitely
        if (getEvaluations() > getMaxEvaluations()) { // Adjust according to your function or framework
            throw new TooManyEvaluationsException(getMaxEvaluations());
        }
    }
}
