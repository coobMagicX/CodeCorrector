protected final double doSolve() throws TooManyEvaluationsException {
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    incrementEvaluationCount();
    double f1 = computeObjectiveValue(x1);
    incrementEvaluationCount();

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
        double x = x1 - (f1 * (x1 - x0)) / (f1 - f0);
        double fx = computeObjectiveValue(x);
        incrementEvaluationCount();

        if (fx == 0.0) {
            return x;
        }

        if (f1 * fx < 0) {
            x0 = x1;
            f0 = f1;
            inverted = !inverted;
        } else {
            // Adjustments based on methods omitted for brevity
        }

        x1 = x;
        f1 = fx;

        if (Math.abs(f1) <= ftol || Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return x1; // Include logic for allowed sides
        }
    }
}

private void incrementEvaluationCount() throws TooManyEvaluationsException {
    if (++evaluations > maxEvaluations) {
        throw new TooManyEvaluationsException(maxEvaluations);
    }
}
