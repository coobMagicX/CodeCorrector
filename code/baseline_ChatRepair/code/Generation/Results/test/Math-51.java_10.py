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
    double lastX = Double.POSITIVE_INFINITY;
    
    while (true) {
        // Avoid division by zero
        if (f1 == f0) {
            throw new ConvergenceException("Convergence error: f1 and f0 are equal, division by zero");
        }

        final double x = x1 - (f1 * (x1 - x0)) / (f1 - f0);
        final double fx = computeObjectiveValue(x);

        if (fx == 0.0) {
            return x;
        }

        if (f1 * fx < 0) {
            x0 = x1;
            f0 = f1;
            inverted = !inverted;
        } else {
            x0 = x;
            f0 *= 0.5;
        }

        x1 = x;
        f1 = fx;

        if (Math.abs(f1) <= ftol) {
            return x1;
        }

        if (Math.abs(x1 - lastX) < Math.max(rtol * Math.abs(x1), atol)) {
            return x1; // No significant progress, terminate loop and return x1
        }

        lastX = x1;

        if (getEvaluations() > getMaxEvaluations()) {
            throw new TooManyEvaluationsException(getMaxEvaluations());
        }
    }
}
