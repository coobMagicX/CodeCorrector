protected final double doSolve() {
    final int MAX_EVAL = 10000; // Maximum allowable evaluations for safety.
    int evaluations = 0;

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

    while (evaluations < MAX_EVAL) {
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);
        evaluations++;

        if (fx == 0.0 || FastMath.abs(f1 - f0) < ftol) {
            return x;
        }

        // Update the bounds with the new approximation
        if (f1 * fx < 0) {
            x0 = x1;
            f0 = f1;
            inverted = !inverted;
        } else {
            if (FastMath.abs(f0) > FastMath.abs(f1)) {
                f0 *= 0.5;
            } else {
                f0 *= f1 / (f1 + fx);
            }
        }

        x1 = x;
        f1 = fx;

        if (FastMath.abs(fx) <= ftol) {
            return x1;
        }

        // Check for convergence
        if (FastMath.abs(x1 - x0) < Math.max(rtol * FastMath.abs(x1), atol)) {
            return x1;
        }
    }
    throw new TooManyEvaluationsException(MAX_EVAL);
}
