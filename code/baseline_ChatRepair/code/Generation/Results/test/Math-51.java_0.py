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
    double xPrev = Double.POSITIVE_INFINITY;

    while (true) {
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);

        if (fx == 0.0 || Math.abs(fx) < ftol) {
            return x; // Root found, or close enough
        }

        if (Math.abs(x - xPrev) <= Math.max(rtol * Math.abs(x), atol)) {
            return x; // Convergence by small delta change
        }

        xPrev = x; // Update previous value

        if (f1 * fx < 0) {
            x0 = x1;
            f0 = f1;
            inverted = !inverted;
        } else {
            // Update 'f0' according to the method used
            if ("ILLINOIS".equals(method)) {
                f0 *= 0.5;
            } else if ("PEGASUS".equals(method)) {
                f0 *= f1 / (f1 + fx);
            }
        }

        x1 = x;
        f1 = fx;

        if (exceedsMaxEvaluations()) {
            throw new TooManyEvaluationsException(maxEvaluations);
        }

        if (Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            // Interval too small to make further progress, return best estimate
            return inverted ? x0 : x1;
        }
    }
}

private boolean exceedsMaxEvaluations() {
    // Implement the method to check if the maximum number of evaluations has been exceeded
    return false; // Example placeholder
}
