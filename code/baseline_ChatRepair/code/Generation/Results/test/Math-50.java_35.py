protected final double doSolve() throws TooManyEvaluationsException {
    // Define the maximum number of evaluations allowed
    int maxEvaluations = getMaxEvaluations(); // Need to define or get this value
    int evaluations = 0;

    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    evaluations++;
    double f1 = computeObjectiveValue(x1);
    evaluations++;

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
        if (evaluations >= maxEvaluations) {
            throw new TooManyEvaluationsException(maxEvaluations);
        }

        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);
        evaluations++;

        if (fx == 0.0) {
            return x;
        }

        if (f1 * fx < 0) {
            x0 = x1;
            f0 = f1;
            inverted = !inverted;
        } else if (...other conditions...) {
            // Existing logic
        }

        x1 = x;
        f1 = fx;

        if (FastMath.abs(f1) <= ftol) {
            // Return logic based on 'allowed' constraints
        }

        if (FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
            // Return logic based on 'allowed' constraints
        }
    }
}
