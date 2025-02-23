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

    final double atol = getAbsoluteAccuracy();
    final double rtol = getRelativeAccuracy();
    final double functionValueAccuracy = getFunctionValueAccuracy();

    boolean inverted = false;

    while (true) {
        final double xMid = x1 - (f1 * (x1 - x0) / (f1 - f0));
        final double fMid = computeObjectiveValue(xMid);

        if (Math.abs(fMid) <= functionValueAccuracy) {
            return xMid;
        }

        if (fMid * f1 < 0) {
            x0 = x1;
            f0 = f1;
            inverted = !inverted;
        } else {
            f0 *= (method == Method.PEGASUS) ? f1 / (f1 + fMid) : 0.5;
        }

        x1 = xMid;
        f1 = fMid;

        // Check for convergence
        double xDiff = Math.abs(x1 - x0);
        if (xDiff <= Math.max(rtol * Math.abs(x1), atol)) {
            return inverted ? x0 : x1;
        }

        // Ensure the number of iterations does not exceed some reasonable number
        // (e.g., too many iterations might indicate an issue with convergence)
        if (getEvaluations() > getMaxEvaluations()) {
            throw new TooManyEvaluationsException(getMaxEvaluations());
        }
    }
}
