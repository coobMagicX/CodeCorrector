public double solve(final UnivariateRealFunction f,
                    final double min, final double max, final double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException {

    clearResult();
    verifySequence(min, initial, max);

    // return the initial guess if it is good enough
    double yInitial = f.value(initial);
    if (Math.abs(yInitial) <= functionValueAccuracy) {
        setResult(initial, 0);
        return result;
    }

    // return the first endpoint if it is good enough
    double yMin = f.value(min);
    if (Math.abs(yMin) <= functionValueAccuracy) {
        setResult(yMin, 0);
        return result;
    }

    // reduce interval if min and initial bracket the root
    if (yInitial * yMin < 0) {
        return solve(f, min, yMin, max, f.value(max), min, yMin);
    }

    // return the second endpoint if it is good enough
    double yMax = f.value(max);
    if (Math.abs(yMax) <= functionValueAccuracy) {
        setResult(yMax, 0);
        return result;
    }

    // reduce interval if initial and max bracket the root
    if (yInitial * yMax < 0) {
        return solve(f, min, yMin, max, f.value(max), initial, yInitial);
    }

    // both endpoints are roots or do not bracket the root
    throw MathRuntimeException.createIllegalArgumentException(
          NON_BRACKETING_MESSAGE, min, max, yMin, yMax);
}

private double solve(final UnivariateRealFunction f,
                     double x0, double y0,
                     double x1, double y1,
                     double x2, double y2)
throws MaxIterationsExceededException, FunctionEvaluationException {

    double delta = x1 - x0;
    double oldDelta = delta;

    int i = 0;
    while (i < maximalIterationCount) {
        if (Math.abs(y2) < Math.abs(y1)) {
            // use the bracket point if is better than last approximation
            x0 = x1;
            x1 = x2;
            x2 = x0;
            y0 = y1;
            y1 = y2;
            y2 = y0;
        }
        if (Math.abs(y1) <= functionValueAccuracy) {
            // Avoid division by very small values. Assume
            // the iteration has converged (the problem may
            // still not be solved, so continue with other roots)
            setResult(x1, 0);
            return result;
        }
        if ((y1 > 0) == (y2 > 0)) {
            x2 = x0;
            y2 = y0;
            delta = x1 - x0;
            oldDelta = delta;
        }
        // ... rest of the method remains the same ...
    }
    throw new MaxIterationsExceededException(maximalIterationCount);
}