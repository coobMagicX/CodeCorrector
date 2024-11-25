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

    // check if min or max is a root
    if (Math.abs(f.value(min)) <= functionValueAccuracy) {
        setResult(min, 0);
        return result;
    }
    if (Math.abs(f.value(max)) <= functionValueAccuracy) {
        setResult(max, 0);
        return result;
    }

    // reduce interval if min and initial bracket the root
    double yMin = f.value(min);
    if (yInitial * yMin < 0) {
        return solve(f, min, max, initial, yInitial, min, yMin);
    }

    // reduce interval if initial and max bracket the root
    double yMax = f.value(max);
    if (yInitial * yMax < 0) {
        return solve(f, initial, max, min, yMin, max, yMax);
    }

    if (yMin * yMax > 0) {
        throw MathRuntimeException.createIllegalArgumentException(
              NON_BRACKETING_MESSAGE, min, max, yMin, yMax);
    }

    // full Brent algorithm starting with provided initial guess
    return solve(f, min, f.value(min), max, f.value(max), initial, yInitial);

}

private double solve(final UnivariateRealFunction f,
                     double x0, double y0,
                     double x1, double y1,
                     double x2, double y2)
throws MaxIterationsExceededException, FunctionEvaluationException {

    // ... rest of the code remains the same ...
}