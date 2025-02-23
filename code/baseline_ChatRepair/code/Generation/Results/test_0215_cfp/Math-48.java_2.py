protected final double doSolve() {
    final int MAX_EVAL = 1000;  // Maximum allowable evaluations
    int evals = 0;  // Evaluation counter

    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    double f1 = computeObjectiveValue(x1);
    evals += 2;  // Account for evaluations

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

    while (evals < MAX_EVAL) {
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);
        evals++;  // Increment evaluation count

        if (fx == 0.0) {
            return x;
        }

        // Update bounds code as before...

        if (FastMath.abs(f1) <= ftol || FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
            // Return condition based on `allowed` criteria as in initial code...
        }
    }

    throw new TooManyEvaluationsException(MAX_EVAL);
}
